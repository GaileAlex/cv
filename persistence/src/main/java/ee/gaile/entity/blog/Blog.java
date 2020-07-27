package ee.gaile.entity.blog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "Blog")
@Table(name = "blog")
public class Blog {

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
    private Date date;

    @OneToMany(targetEntity = Comments.class, cascade = {
            CascadeType.ALL
    })
    @JoinColumn(name = "comment_id")
    private List<Comments> comments = new ArrayList<>();

    public Blog(String headline, String article, byte[] image) {
        this.headline = headline;
        this.article = article;
        this.image = image;
    }

    @PrePersist
    void createdAt() {
        this.date = new Date();
    }
}
