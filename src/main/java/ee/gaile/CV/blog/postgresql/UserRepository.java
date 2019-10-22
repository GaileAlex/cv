package ee.gaile.CV.blog.postgresql;


import ee.gaile.CV.blog.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface UserRepository extends CrudRepository<User, UUID> {

    User findByUsername(String username);
}
