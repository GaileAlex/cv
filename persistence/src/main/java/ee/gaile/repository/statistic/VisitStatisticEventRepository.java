package ee.gaile.repository.statistic;

import ee.gaile.entity.statistics.VisitStatisticsEventsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitStatisticEventRepository extends JpaRepository<VisitStatisticsEventsEntity, Long> {
}
