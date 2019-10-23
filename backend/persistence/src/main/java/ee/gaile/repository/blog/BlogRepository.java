package ee.gaile.repository.blog;

import ee.gaile.repository.entity.blog.Blog;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface BlogRepository extends CrudRepository<Blog, UUID> {
}
