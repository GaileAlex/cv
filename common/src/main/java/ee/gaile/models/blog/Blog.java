package ee.gaile.models.blog;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class Blog {

    private Long id;

    private String headline;

    private String article;

    private byte[] image;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime date;

    private List<CommentModel> comments;

}
