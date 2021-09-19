package ee.gaile.models.blog;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlogWrapper {

    private Long id;

    private String headline;

    private String article;

    private String image;

    private LocalDateTime date;

    private List<CommentModel> comments;

}
