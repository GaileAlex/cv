package ee.gaile.CV.blog.postgresql;

import ee.gaile.CV.blog.model.Comments;
import org.springframework.data.repository.CrudRepository;

public interface CommentsRepository extends CrudRepository<Comments, Long> {
}
