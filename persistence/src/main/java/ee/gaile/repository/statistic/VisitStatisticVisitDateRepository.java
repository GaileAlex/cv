package ee.gaile.repository.statistic;

import ee.gaile.entity.statistics.VisitStatisticVisitDateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitStatisticVisitDateRepository extends JpaRepository<VisitStatisticVisitDateEntity, Long> {
}
