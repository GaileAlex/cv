package ee.gaile.entity.blog;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@Entity(name = "Blog")
@Table(name = "blog")
@NoArgsConstructor
public class BlogEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "blog_headline", length = 200)
    private String headline;

    @Column(name = "blog_article", length = 10485760)
    private String article;

    @Column(name = "blog_image")
    private byte[] image;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "blog_date")
    private LocalDateTime date;

    @OneToMany(mappedBy = "blog")
    private List<CommentEntity> comments;

    public BlogEntity(String headline, String article, byte[] image) {
        this.headline = headline;
        this.article = article;
        this.image = image;
    }

    @PrePersist
    void createdAt() {
        this.date = LocalDateTime.now();
    }
}
