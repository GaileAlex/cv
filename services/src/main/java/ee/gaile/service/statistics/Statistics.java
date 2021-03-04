package ee.gaile.service.statistics;

import ee.gaile.entity.statistics.VisitStatisticVisitDate;
import ee.gaile.entity.statistics.VisitStatistics;
import ee.gaile.repository.statistic.VisitStatisticVisitDateRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Map;

public interface Statistics {

    /**
     * Saves user data to a database
     *
     * @param request - HttpServletRequest
     * @return - sessionId
     */
    Map<String, String> setUserStatistics(HttpServletRequest request);

    /**
     * Saves the date the user visited the site
     *
     * @param visitStatistics     - list of user visits
     * @param visitDateRepository - visit date statistics repository
     */
    default void setVisitDate(VisitStatistics visitStatistics,
                              VisitStatisticVisitDateRepository visitDateRepository) {
        VisitStatisticVisitDate visitDate = new VisitStatisticVisitDate();
        visitDate.setVisitStatistics(visitStatistics);
        visitDate.setVisitDate(LocalDateTime.now());

        visitDateRepository.save(visitDate);
    }
}
