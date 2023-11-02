package es.dgv93.telegramemojicounter.chat;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Data
public class Chat {
    private Long id;
    private String name;
    private String type;
    private List<Message> messages;

    @Data
    public static class Message {
        private String id;
        private MessageType type;
        private LocalDateTime date;
        private Long date_unixtime;
        private String actor;
        private String actor_id;
        private String from;
        private String from_id;
        private String action;
        private String title;
        private List<String> members;
        @JsonDeserialize(using = TextChatDeserializer.class)
        private String text;
        private List<HashMap<String, String>> text_entities;

    }


}
