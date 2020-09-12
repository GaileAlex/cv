package ee.gaile.dto.blog;

import ee.gaile.entity.blog.Comments;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BlogWrapper {
    private Long id;

    private String headline;

    private String article;

    private String image;

    private Date date;

    private List<Comments> comments;
}
