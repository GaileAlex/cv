package ee.gaile.service.repository.blog;

import ee.gaile.entity.blog.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {

    @Query(value = "select * from blog order by blog_date desc ", nativeQuery = true)
    List<Blog> findAllOrderByDate();

    @Query(value = "select * from blog left join comments c on blog.id = c.blog_id where blog.id = :blogId ",
            nativeQuery = true)
    Blog findBlogById(Long blogId);
}
