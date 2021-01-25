package ee.gaile.service.security;

import ee.gaile.entity.users.Users;
import ee.gaile.enums.EnumRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Users findByRole(@Param("role") EnumRoles role);

}
