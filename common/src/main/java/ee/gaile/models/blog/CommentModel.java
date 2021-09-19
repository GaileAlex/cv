package ee.gaile.models.blog;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author Aleksei Gaile 19-Sep-21
 */
@Getter
@Setter
public class CommentModel {

    private Long id;

    private String comment;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime date;

    private String userName;

    @JsonIgnore
    private Blog blog;

}
