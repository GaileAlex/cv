package ee.gaile.models.blog;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentWrapper {

    private String comment;

    private Long blogId;

}
