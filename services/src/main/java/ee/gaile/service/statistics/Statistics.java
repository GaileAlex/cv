package ee.gaile.service.statistics;

import ee.gaile.entity.statistics.VisitStatisticVisitDate;
import ee.gaile.entity.statistics.VisitStatistics;
import ee.gaile.repository.statistic.VisitStatisticVisitDateRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;

public interface Statistics {

    Map<String, String> setUserStatistics(HttpServletRequest request);

    default void setVisitDate(VisitStatistics visitStatistics,
                              VisitStatisticVisitDateRepository visitDateRepository) {
        VisitStatisticVisitDate visitDate = new VisitStatisticVisitDate();
        visitDate.setVisitStatistics(visitStatistics);
        visitDate.setVisitDate(LocalDateTime.now());

        visitDateRepository.save(visitDate);
    }
}
