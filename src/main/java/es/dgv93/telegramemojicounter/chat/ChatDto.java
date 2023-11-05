package es.dgv93.telegramemojicounter.chat;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
public class ChatDto {
    @JsonProperty("data")
    private List<Map<String, Object>> graphData;
    @JsonProperty("users")
    private List<String> users;
    @JsonProperty("stats")
    private List<SummaryStats> stats;
    public ChatDto() {
        super();
    }
    @Data
    public static class ChatData {
        @JsonProperty("user_id")
        private String user;
        private String name;
        private LocalDateTime date;

        public ChatData() {
            super();
        }
        public static ChatData from(Chat.Message message) {
            var data = new ChatData();
            data.setDate(message.getDate());
            data.setName(message.getFrom());
            data.setUser(message.getFrom_id());
            return data;
        }
    }
}
