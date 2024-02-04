package ee.gaile.entity.blog;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity(name = "Comments")
@Table(name = "comments")
@NoArgsConstructor
public class CommentEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "comment", length = 2000)
    private String comment;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date")
    private LocalDateTime date;

    @Column(name = "user_name")
    private String userName;

    @ManyToOne
    @JoinColumn(name = "blog_id", nullable = false)
    @JsonIgnore
    private BlogEntity blog;

    public CommentEntity(String comment, String userName, BlogEntity blog) {
        this.comment = comment;
        this.userName = userName;
        this.blog = blog;
    }

    @PrePersist
    void createdAt() {
        this.date = LocalDateTime.now();
    }
}
