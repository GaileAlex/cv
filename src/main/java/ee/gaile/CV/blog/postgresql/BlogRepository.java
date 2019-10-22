package ee.gaile.CV.blog.postgresql;

import ee.gaile.CV.blog.model.Blog;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface BlogRepository extends CrudRepository<Blog, UUID> {

}
