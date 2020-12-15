package ee.gaile.entity.blog;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Comments")
@Table(name = "comments")
public class Comments {

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
    private Blog blog;

    public Comments(String comment, String userName, Blog blog) {
        this.comment = comment;
        this.userName = userName;
        this.blog = blog;
    }

    @PrePersist
    void createdAt() {
        this.date = LocalDateTime.now();
    }
}
