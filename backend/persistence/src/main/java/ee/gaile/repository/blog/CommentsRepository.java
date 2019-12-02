package ee.gaile.repository.blog;

import ee.gaile.repository.entity.blog.Comments;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentsRepository extends CrudRepository<Comments, UUID> {
}
