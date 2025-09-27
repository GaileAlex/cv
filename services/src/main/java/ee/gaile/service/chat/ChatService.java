package ee.gaile.service.chat;

import ee.gaile.entity.chat.ChatMessageEntity;
import ee.gaile.models.chat.ChatMessage;
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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository repo;
    private final RestTemplate restTemplate;

    @Value("${ollama.url}")
    private String ollamaUrl;

    private static final String CHAT_DESCRIPTION = "Create a short description of this chat in no more than 10 words using this phrase - ";

    @Transactional
    public ChatMessage chat(String username,
                            String sessionId,
                            Map<String, String> message) {

        String userMessage = message.get("message");
        if (userMessage == null) {
            userMessage = "";
        }

        if (sessionId == null || sessionId.equals("null") || sessionId.isBlank()) {
            sessionId = UUID.randomUUID().toString();
        }

        boolean hasDescription = repo.existsBySessionIdAndSessionDescriptionIsNotNull(sessionId);

        ChatMessageEntity userMsg = new ChatMessageEntity();
        userMsg.setUserName(username);
        userMsg.setSessionId(sessionId);
        if (!hasDescription) {
            userMsg.setSessionDescription(getAnswerFromOllama(CHAT_DESCRIPTION + userMessage));
        }
        userMsg.setRole("user");
        userMsg.setContent(userMessage);
        userMsg.setCreatedAt(LocalDateTime.now());
        repo.save(userMsg);

        List<ChatMessageEntity> history = repo.findBySessionIdOrderByCreatedAtAsc(sessionId);

        StringBuilder prompt = new StringBuilder();
        for (ChatMessageEntity m : history) {
            prompt.append(m.getRole()).append(": ").append(m.getContent()).append("\n");
        }
        prompt.append("assistant:");

        String answer = getAnswerFromOllama(prompt.toString());

        ChatMessageEntity assistantMsg = new ChatMessageEntity();
        assistantMsg.setUserName(username);
        assistantMsg.setSessionId(sessionId);
        if (!hasDescription) {
            assistantMsg.setSessionDescription(userMsg.getSessionDescription());
        }
        assistantMsg.setRole("assistant");
        assistantMsg.setContent(answer);
        assistantMsg.setCreatedAt(LocalDateTime.now());
        repo.save(assistantMsg);

        return new ChatMessage(sessionId, userMessage, answer);
    }

    @Transactional(readOnly = true)
    public List<ChatMessageEntity> getHistory(String sessionId) {
        if (sessionId == null || sessionId.isBlank()) {
            throw new IllegalArgumentException("sessionId is required");
        }

        return repo.findBySessionIdOrderByCreatedAtAsc(sessionId);
    }

    public Map<String, String> getSessionsHistory(String username) {
        Map<String, String> sessions = new LinkedHashMap<>();
        List<Object[]> rows = repo.findSessionsWithDescription(username);
        for (Object[] row : rows) {
            String sessionId = (String) row[0];
            String description = (String) row[1];
            sessions.put(sessionId, description);
        }
        return sessions;
    }

    private String getAnswerFromOllama(String prompt) {
        Map<String, Object> body = new HashMap<>();
        body.put("system", SystemInstruction.SYSTEM_INSTRUCTION);
        body.put("model", "llama3:8b");
        body.put("temperature", 0.7);
        body.put("top_p", 0.9);
        body.put("num_ctx", 8192);
        body.put("stream", false);
        body.put("prompt", prompt);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        String answer;
        try {
            ResponseEntity<Map> response =
                    restTemplate.postForEntity(ollamaUrl, request, Map.class);
            answer = (String) response.getBody().get("response");
            if (answer == null) answer = "";
        } catch (Exception e) {
            answer = "Ошибка вызова модели: " + e.getMessage();
        }

        return answer;
    }

}
