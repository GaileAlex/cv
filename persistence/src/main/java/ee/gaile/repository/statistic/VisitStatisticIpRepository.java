package ee.gaile.repository.statistic;

import ee.gaile.entity.statistics.VisitStatisticUserIpEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitStatisticIpRepository extends JpaRepository<VisitStatisticUserIpEntity, Long> {
}
