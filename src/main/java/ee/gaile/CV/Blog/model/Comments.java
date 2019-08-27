package ee.gaile.CV.Blog.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "comments")
public class Comments implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn (name="comment_id")
    private Blog blog;

    @NotBlank(message = "Comment is required")
    private String comment;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    private String username;



    @PrePersist
    void createdAt() {
        this.date = new Date();

    }
}
