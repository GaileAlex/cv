package ee.gaile.repository.blog;

import ee.gaile.repository.entity.blog.User;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {
    User findByUsername(String username);
}
