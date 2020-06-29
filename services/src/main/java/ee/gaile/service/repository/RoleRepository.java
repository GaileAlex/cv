package ee.gaile.service.repository;


import ee.gaile.entity.enums.EnumRoles;
import ee.gaile.entity.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(EnumRoles name);
}
