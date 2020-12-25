package ee.gaile.repository;

import ee.gaile.entity.ProxyList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProxyRepository extends JpaRepository<ProxyList, Long> {
}
