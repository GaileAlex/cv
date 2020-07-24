package ee.gaile.entity.blog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "blog_headline", length = 2000)
    private String headline;

    @Column(name = "blog_article", length = 10485760)
    private String article;

    @Lob
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

}
