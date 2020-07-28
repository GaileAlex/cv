package ee.gaile.entity.blog;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
public class BlogWrapper {

    private Long id;

    private String headline;

    private String article;

    private String image;

    private Date date;

    private List<Comments> comments;
}
