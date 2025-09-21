package ee.gaile.repository.chat;

import ee.gaile.entity.chat.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author Aleksei Gaile 21 Sept 2025
 */
public interface ChatMessageRepository extends JpaRepository<ChatMessageEntity, Long> {

    List<ChatMessageEntity> findBySessionIdOrderByCreatedAtAsc(String sessionId);

    boolean existsBySessionIdAndSessionDescriptionIsNotNull(String sessionId);

    @Query("""
    SELECT m.sessionId, m.sessionDescription, MIN(m.createdAt)
    FROM ChatMessageEntity m
    WHERE m.userName = :username
      AND m.sessionDescription IS NOT NULL
    GROUP BY m.sessionId, m.sessionDescription
    ORDER BY MIN(m.createdAt)
    """)
    List<Object[]> findSessionsWithDescription(@Param("username") String username);

}
