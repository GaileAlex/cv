package ee.gaile.service.repository.mindly;

import ee.gaile.entity.mindly.Mindly;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MindlyRepository extends JpaRepository<Mindly, Long> {
}
