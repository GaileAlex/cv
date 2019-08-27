package ee.gaile.CV.Blog.postgresql;

import ee.gaile.CV.Blog.model.Comments;
import org.springframework.data.repository.CrudRepository;

public interface CommentsRepository extends CrudRepository<Comments, Long> {
}
