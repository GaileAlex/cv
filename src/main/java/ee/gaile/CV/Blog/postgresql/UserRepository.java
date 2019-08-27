package ee.gaile.CV.Blog.postgresql;


import ee.gaile.CV.Blog.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {

    User findByUsername(String username);
}