package ee.gaile.repository.chat;

import ee.gaile.entity.chat.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author Aleksei Gaile 21 Sept 2025
 */
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

    List<ChatMessageEntity> findByUserNameOrderByCreatedAtAsc(String sessionId);

}
