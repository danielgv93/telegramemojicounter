package es.dgv93.telegramemojicounter.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ChatService {
    public ChatDto transform(Chat chat, String emoji, Map<String, String> options) {
        var month = options.get("month");

        var chatData =  chat.getMessages().stream()
                .filter(message -> message.getType() == MessageType.MESSAGE)
                .filter(message -> message.getText().contains(emoji))
                .filter(message -> {
                    if (month.equalsIgnoreCase("ALL")) {
                        return true;
                    }
                    return message.getDate().getMonth().name().equalsIgnoreCase(month);
                })
                .map(ChatDto.ChatData::from)
                .toList();

        var users = chatData.stream().map(ChatDto.ChatData::getName).distinct().toList();

        var data = groupAndConvert(chatData);

        var dto = new ChatDto();
        dto.setData(data);
        dto.setUsers(users);
        return dto;
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
