package ee.gaile.models.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Aleksei Gaile 22 Sept 2025
 */
@Setter
@Getter
@AllArgsConstructor
public class ChatMessage {

    String sessionId;

    String userMessage;

    String message;

}
