package ee.gaile.CV.blog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "blog")
public class Blog {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Headline is required")
    private String headline;

    @NotBlank(message = "Article is required")
    @Column(length = 10485760)
    private String article;

    @Lob
    @NotNull(message = "Image is required")
    private byte[] image;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @OneToMany (mappedBy="blog", fetch=FetchType.EAGER, orphanRemoval=true)
    private List<Comments> items = new ArrayList<>();

}
