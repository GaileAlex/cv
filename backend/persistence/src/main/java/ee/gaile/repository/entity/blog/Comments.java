package ee.gaile.repository.entity.blog;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comments implements Serializable {

    @Id
    @Column(name = "comments_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    @Type(type = "uuid-char")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "comment_blog")
    private Blog blog;

    @NotBlank(message = "Comment is required")
    @Column(name = "blog_comment", length = 2000)
    private String comment;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "blog_date")
    private Date date;

    @Column(name = "blog_username")
    private String username;

    @PrePersist
    void createdAt() {
        this.date = new Date();
    }
}
