package ee.gaile.controller.chat;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @author Aleksei Gaile 7 Sept 2025
 */
@RestController
@RequestMapping("/api/chat")
public class ChatController {

    @Value("${ollama.url}")
    private String ollamaUrl;

    @PostMapping("/generate")
    public ResponseEntity<String> generate(@RequestBody Map<String,Object> body) {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        body.put("model", "qwen2.5:7b-instruct");
        HttpEntity<Map<String,Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = rest.postForEntity(ollamaUrl, request, String.class);
        return ResponseEntity.ok(response.getBody());
    }

}
