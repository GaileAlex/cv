package ee.gaile.service.security;

import ee.gaile.entity.users.UserEntity;
import ee.gaile.enums.EnumRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    UserEntity findByRole(@Param("role") EnumRoles role);

}
