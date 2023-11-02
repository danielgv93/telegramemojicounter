package es.dgv93.telegramemojicounter.chat;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TextChatDeserializer extends JsonDeserializer<Object> {
    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        if (node.isArray()) {
            List<String> list = new ArrayList<>();
            for (JsonNode element : node) {
                if (element.isTextual()) {
                    list.add(element.asText());
                }
                if (element.isContainerNode()) {
                    list.add(element.get("text").asText());
                }
            }
            return String.join(" ", list);
        } else {
            return node.asText();
        }
    }
}
