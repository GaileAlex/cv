package ee.gaile.entity.blog;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
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

    @Type(type = "org.hibernate.type.BinaryType")
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
