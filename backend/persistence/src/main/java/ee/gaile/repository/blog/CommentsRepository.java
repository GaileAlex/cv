package ee.gaile.repository.blog;

import ee.gaile.repository.entity.blog.Comments;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface CommentsRepository extends CrudRepository<Comments, UUID> {
}
