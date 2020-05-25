package ee.gaile.repository.entity.blog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "blog")
public class Blog {

    @Id
    @Column(name = "blog_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    @Type(type = "uuid-char")
    private UUID id;

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

    /*@OneToMany(mappedBy = "blog", fetch = FetchType.EAGER, orphanRemoval = true)
    @Singular
    @JsonIgnore
    private List<Comments> items = new ArrayList<>();*/

}
