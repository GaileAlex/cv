package ee.gaile.repository.statistic;

import ee.gaile.entity.statistics.VisitStatisticVisitDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VisitStatisticVisitDateRepository extends JpaRepository<VisitStatisticVisitDate, Long> {
}
