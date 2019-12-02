package ee.gaile.repository.blog;

import ee.gaile.repository.entity.blog.Blog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BlogRepository extends CrudRepository<Blog, UUID> {
}
