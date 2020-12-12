package ee.gaile.repository.statistic;

import ee.gaile.entity.statistics.VisitStatisticUserIp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitStatisticIpRepository extends JpaRepository<VisitStatisticUserIp, Long> {
}
