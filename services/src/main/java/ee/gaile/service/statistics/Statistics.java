package ee.gaile.service.statistics;

import ee.gaile.entity.statistics.VisitStatisticVisitDate;
import ee.gaile.entity.statistics.VisitStatistics;
import ee.gaile.repository.statistic.VisitStatisticVisitDateRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface Statistics {

    void setUserStatistics(HttpServletRequest request);

    VisitStatistics getUserTotalTimeOnSite(HttpServletRequest request);

    default VisitStatistics setTime(VisitStatistics user, String date) {
        LocalDateTime userEntry = user.getLastVisit();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime userOut = LocalDateTime.parse(date, formatter);

        long between = Duration.between(userEntry, userOut).toMillis();

        try {
            user.setTotalTimeOnSite(user.getTotalTimeOnSite() + between);
        } catch (NullPointerException e) {
            user.setTotalTimeOnSite(between);
        }
        return user;
    }

    default void setVisitDate(VisitStatistics visitStatistics,
                              VisitStatisticVisitDateRepository visitDateRepository) {
        VisitStatisticVisitDate visitDate = new VisitStatisticVisitDate();
        visitDate.setVisitStatistics(visitStatistics);
        visitDate.setVisitDate(LocalDateTime.now());

        visitDateRepository.save(visitDate);

    }
}
