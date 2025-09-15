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

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
    public ResponseEntity<String> generate(@RequestBody Map<String,Object> body) throws IOException {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        InputStream is = getClass().getClassLoader().getResourceAsStream("Vikhr.modelfile");
        String systemInstruction = new String(is.readAllBytes(), StandardCharsets.UTF_8);
        body.put("system", systemInstruction);
        body.put("model", "llama3:8b");
        body.put("temperature", 0.7);
        body.put("top_p", 0.9);
        body.put("num_ctx", 8192);

        HttpEntity<Map<String,Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = rest.postForEntity(ollamaUrl, request, String.class);
        return ResponseEntity.ok(response.getBody());
    }

}
