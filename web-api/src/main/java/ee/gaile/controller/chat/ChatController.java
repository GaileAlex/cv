package ee.gaile.controller.chat;

import ee.gaile.entity.chat.ChatMessageEntity;
import ee.gaile.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author Aleksei Gaile 7 Sept 2025
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/{username}/{sessionId}")
    public ResponseEntity<String> sendMessage(
            @PathVariable String username,
            @PathVariable String sessionId,
            @RequestBody Map<String, String> message)  {

        String answer = chatService.chat(username, sessionId, message);
        return ResponseEntity.ok(answer);
    }

    @GetMapping("/{sessionId}/history")
    public ResponseEntity<List<ChatMessageEntity>> getHistory(@PathVariable String sessionId) {
        return ResponseEntity.ok(chatService.getHistory(sessionId));
    }

    @GetMapping("/{username}/sessions")
    public ResponseEntity<Map<String, String>> getSessionsHistory(@PathVariable String username) {
        return ResponseEntity.ok(chatService.getSessionsHistory(username));
    }

}
