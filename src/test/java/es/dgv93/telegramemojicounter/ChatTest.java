package es.dgv93.telegramemojicounter;

import es.dgv93.telegramemojicounter.chat.Chat;
import es.dgv93.telegramemojicounter.chat.ChatService;
import es.dgv93.telegramemojicounter.utils.FileLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class ChatTest {

    private static ChatService chatService;
    @BeforeAll
    public static void setUp(){
        chatService = new ChatService();
    }

    @Test
    void parseTest() throws Exception {
        var emoji = "\uD83D\uDCA9";
        var chat = FileLoader.loadJson("/mocks/telegram-chat.json", Chat.class);
        var dto = chatService.transform(chat, emoji, Map.of("month", "ALL", "year", "ALL"));
        Assertions.assertEquals(
                List.of("Daniel", "Karla \uD83D\uDC08", "Sergio Niño Polla", "Álvaro"),
                dto.getUsers());
        Assertions.assertEquals(
                Map.of("date", "2023-01-14", "Daniel", 1),
                dto.getGraphData().get(0));
    }
}
