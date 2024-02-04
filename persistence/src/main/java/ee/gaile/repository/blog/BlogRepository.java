package ee.gaile.repository.blog;

import ee.gaile.entity.blog.BlogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<BlogEntity, Long> {

    @Query(value = "select * from blog order by blog_date desc ", nativeQuery = true)
    List<BlogEntity> findAllOrderByDate();

    @Query(value = "select b.* from blog b left join comments c on b.id = c.blog_id where b.id = :blogId ",
            nativeQuery = true)
    BlogEntity findBlogById(@Param("blogId") Long blogId);
}
