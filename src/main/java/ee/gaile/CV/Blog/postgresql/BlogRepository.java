package ee.gaile.CV.Blog.postgresql;

import ee.gaile.CV.Blog.model.Blog;
import org.springframework.data.repository.CrudRepository;

public interface BlogRepository extends CrudRepository<Blog, Long> {

}
