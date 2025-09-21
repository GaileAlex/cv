package ee.gaile.service.chat;

import ee.gaile.entity.chat.ChatMessageEntity;
import ee.gaile.repository.chat.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository repo;
    private final RestTemplate restTemplate;

    @Value("${ollama.url}")
    private String ollamaUrl;

    /**
     * Отправить запрос в Ollama, сохранить сообщение пользователя и ответ модели
     */
    @Transactional
    public String chat(String username, Map<String, String> message) {
        String userMessage = message.get("message");
        if (userMessage == null) {
            userMessage = "";
        }

        ChatMessageEntity userMsg = new ChatMessageEntity();
        userMsg.setUserName(username);
        userMsg.setRole("user");
        userMsg.setContent(userMessage);
        userMsg.setCreatedAt(LocalDateTime.now());
        repo.save(userMsg);

        List<ChatMessageEntity> history = repo.findByUserNameOrderByCreatedAtAsc(username);
        StringBuilder prompt = new StringBuilder();
        for (ChatMessageEntity m : history) {
            prompt.append(m.getRole()).append(": ").append(m.getContent()).append("\n");
        }
        prompt.append("assistant:");

        Map<String, Object> body = new HashMap<>();
        body.put("system", SystemInstruction.SYSTEM_INSTRUCTION);
        body.put("model", "llama3:8b");
        body.put("temperature", 0.7);
        body.put("top_p", 0.9);
        body.put("num_ctx", 8192);
        body.put("stream", false);
        body.put("prompt", prompt.toString());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        String answer;
        try {
            ResponseEntity<Map> response =
                    restTemplate.postForEntity(ollamaUrl, request, Map.class);
            answer = (String) response.getBody().get("response");
            ;
            if (answer == null) answer = "";
        } catch (Exception e) {
            answer = "Ошибка вызова модели: " + e.getMessage();
        }

        // сохраняем ответ модели
        ChatMessageEntity assistantMsg = new ChatMessageEntity();
        assistantMsg.setUserName(username);
        assistantMsg.setRole("assistant");
        assistantMsg.setContent(answer);
        assistantMsg.setCreatedAt(LocalDateTime.now());
        repo.save(assistantMsg);

        return answer;
    }

    /**
     * Получить всю историю разговора
     */
    @Transactional(readOnly = true)
    public List<ChatMessageEntity> getHistory(String username) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("sessionId is required");
        }
        return repo.findByUserNameOrderByCreatedAtAsc(username);
    }
}
