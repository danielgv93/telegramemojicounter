package es.dgv93.telegramemojicounter.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MessageType {
    @JsonProperty("service")
    SERVICE,
    @JsonProperty("message")
    MESSAGE


}
