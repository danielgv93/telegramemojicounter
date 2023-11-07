package es.dgv93.telegramemojicounter.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ChatService {
    public ChatDto transform(Chat chat, String emoji, Map<String, String> options) {
        var month = options.get("month");
        var year = options.get("year");

        var chatData = applyFilter(chat, emoji, options, month, year);

        var graphData = groupAndConvert(chatData);
        var users = chatData.stream().map(ChatDto.ChatData::getName).distinct().sorted().toList();
        var statsByUser = getStatsByUser(month, chatData, users);


        var dto = new ChatDto();
        dto.setGraphData(graphData);
        dto.setUsers(users);
        dto.setStatsByUser(statsByUser);
        dto.setGeneralStats(getGeneralStats(statsByUser));
        return dto;
    }

    private static List<Stats.GeneralStats> getGeneralStats(List<Stats.StatsByUser> statsByUsers) {
        return Stream.of(StatsType.values())
                .map(statType -> {
                    var stats = new HashMap<String, Double>();
                    for (Stats.StatsByUser statsByUser : statsByUsers) {
                        stats.put(statsByUser.getUser(), statsByUser.getStatsByUser().get(statType.getName()));
                    }
                    return Stats.GeneralStats.builder()
                            .statName(statType.getName())
                            .summaryStats(stats)
                            .build();
                })
                .toList();
    }

    private static List<Stats.StatsByUser> getStatsByUser(String month, List<ChatDto.ChatData> chatData, List<String> users) {
        return users.stream()
                .map(user -> {
                    var userChatData = chatData.stream().filter(data -> data.getName().equalsIgnoreCase(user)).toList();
                    var total = userChatData.size();
                    var days = month.equalsIgnoreCase("ALL") ? 365 : Month.valueOf(month.toUpperCase()).getDays();
                    var mean = total / (double) days;
                    var groupedData = new HashMap<LocalDateTime, Integer>();
                    for (var item : userChatData) {
                        var date = item.getDate().withHour(0).withMinute(0).withSecond(0).withNano(0);
                        if (!groupedData.containsKey(date)) {
                            groupedData.put(date, 0);
                        }
                        groupedData.put(date, groupedData.get(date) + 1);
                    }
                    var max = Collections.max(groupedData.values());
                    var min = Collections.min(groupedData.values());
                    var stats = Map.of(
                            StatsType.TOTAL.getName(), (double) total,
                            StatsType.MEAN.getName(), mean,
                            StatsType.MAX.getName(), (double) max);
                    return Stats.StatsByUser.builder()
                            .user(user)
                            .statsByUser(stats)
                            .build();
                })
                .toList();
    }

    private static List<ChatDto.ChatData> applyFilter(Chat chat, String emoji, Map<String, String> options, String month, String year) {
        return chat.getMessages().stream()
                .filter(message -> message.getType() == MessageType.MESSAGE)
                .filter(message -> message.getText().contains(emoji))
                .filter(message -> {
                    if (year.equalsIgnoreCase("ALL")) {
                        return true;
                    }
                    return message.getDate().getYear() == Integer.parseInt(options.get("year"));
                })
                .filter(message -> {
                    if (month.equalsIgnoreCase("ALL")) {
                        return true;
                    }
                    return message.getDate().getMonth().name().equalsIgnoreCase(month);
                })
                .map(ChatDto.ChatData::from)
                .toList();
    }

    private List<Map<String, Object>> groupAndConvert(List<ChatDto.ChatData> chatData) {
        // Step 1: Group by date and count occurrences of each name
        Map<String, Map<String, Integer>> groupedData = new HashMap<>();
        for (var item : chatData) {
            String date = item.getDate().toString().split("T")[0];

            if (!groupedData.containsKey(date)) {
                groupedData.put(date, new HashMap<>());
            }


            if (!groupedData.get(date).containsKey(item.getName())) {
                groupedData.get(date).put(item.getName(), 0);
            }

            groupedData.get(date).put(item.getName(), groupedData.get(date).get(item.getName()) + 1);
        }
        // Step 2: Convert to required format
        List<Map<String, Object>> newFormatData = new ArrayList<>();
        for (Map.Entry<String, Map<String, Integer>> entry : groupedData.entrySet()) {
            String date = entry.getKey();
            Map<String, Integer> dateData = entry.getValue();
            Map<String, Object> newFormatItem = new HashMap<>();
            newFormatItem.put("date", date);

            for (Map.Entry<String, Integer> dateEntry : dateData.entrySet()) {
                newFormatItem.put(dateEntry.getKey(), dateEntry.getValue());
            }

            newFormatData.add(newFormatItem);
        }
        // Step 3: Sort data by date
        newFormatData.sort((a, b) -> {
            try {
                return new SimpleDateFormat("yyyy-MM-dd").parse(a.get("date").toString())
                        .compareTo(new SimpleDateFormat("yyyy-MM-dd").parse(b.get("date").toString()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        });
        return newFormatData;
    }

}
