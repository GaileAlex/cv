package ee.gaile.entity.chat;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "chat_message")
@NoArgsConstructor
public class ChatMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_name", length = 50, nullable = false)
    private String userName;         // уникальный идентификатор беседы (UUID)

    @Column(name = "role", length = 20, nullable = false)
    private String role;              // "user" или "assistant"

    @Lob
    @Column(name = "content")
    private String content;           // текст сообщения

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public ChatMessageEntity(String userName, String role, String content) {
        this.userName = userName;
        this.role = role;
        this.content = content;
    }
}
