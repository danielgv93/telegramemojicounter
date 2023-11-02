package es.dgv93.telegramemojicounter.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;
    private final ObjectMapper objectMapper;

    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ChatDto parse(@RequestBody Map<String, Object> body) {
        var chat = objectMapper.convertValue(body.get("chat"), Chat.class);
        var emoji = (String) body.get("emoji");
        var options = objectMapper.convertValue(body.get("options"), Map.class);
        if (chat == null || emoji == null) {
            return null;
        }
        return chatService.transform(chat, emoji, options);
    }
}
