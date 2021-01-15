package ee.gaile.repository.statistic;

import ee.gaile.entity.statistics.VisitStatisticsEvents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitStatisticEventRepository extends JpaRepository<VisitStatisticsEvents, Long> {
}
