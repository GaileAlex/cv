package ee.gaile.repository.mindly;

import ee.gaile.entity.mindly.MindlyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MindlyRepository extends JpaRepository<MindlyEntity, Long> {
}
