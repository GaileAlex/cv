package ee.gaile.repository.proxy;

import ee.gaile.entity.proxy.ProxySiteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProxySitesRepository extends JpaRepository<ProxySiteEntity, Long> {
}
