package ee.gaile.service.statistics;

import ee.gaile.entity.statistics.VisitStatistics;
import ee.gaile.entity.statistics.VisitStatisticsEvents;
import ee.gaile.repository.statistic.VisitStatisticEventRepository;
import ee.gaile.repository.statistic.VisitStatisticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Service for working with user events
 *
 * @author Aleksei Gaile
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserEvents {
    private final VisitStatisticsRepository visitStatisticsRepository;
    private final VisitStatisticEventRepository visitStatisticEventRepository;

    /**
     * Saves user actions on the site in the database
     *
     * @param request - HttpServletRequest
     */
    public void setUserEvent(HttpServletRequest request) {
        String sessionId;
        if (!request.getHeader("userId").equals("undefined")) {
            sessionId = request.getHeader("userId");
        } else if (!request.getHeader("sessionStorageUserId").equals("undefined")) {
            sessionId = request.getHeader("userId");
        } else {
            return;
        }

        setEvent(request, sessionId);
    }

    /**
     * Saves the time the user spends on the site
     *
     * @param request - HttpServletRequest
     */
    public void setUserTotalTimeOnSite(HttpServletRequest request) {
        Optional<VisitStatistics> visitStatistics =
                visitStatisticsRepository.findBySessionId(request.getHeader("userId"));

        VisitStatistics user = visitStatistics.orElseThrow(NullPointerException::new);

        LocalDateTime userEntry = user.getLastVisit();
        LocalDateTime userOut = LocalDateTime.now();

        long between = Duration.between(userEntry, userOut).toMillis();

        try {
            user.setTotalTimeOnSite(user.getTotalTimeOnSite() + between);
        } catch (NullPointerException e) {
            user.setTotalTimeOnSite(between);
        }

    }

    /**
     * Saves user actions on the site in the database
     *
     * @param request   - HttpServletRequest
     * @param sessionId - session ID
     */
    public void setEvent(HttpServletRequest request, String sessionId) {
        Optional<VisitStatistics> visitStatisticsByNameOptional =
                visitStatisticsRepository.findBySessionId(sessionId);
        VisitStatistics user = visitStatisticsByNameOptional.orElseThrow(NullPointerException::new);

        if (user.getLastEvent() == null) {
            return;
        }
        long between = Duration.between(user.getLastEvent(), LocalDateTime.now()).toMillis();

        user.setLastEvent(LocalDateTime.now());

        try {
            user.setTotalTimeOnSite(user.getTotalTimeOnSite() + between);
        } catch (NullPointerException e) {
            user.setTotalTimeOnSite(between);
        }

        VisitStatisticsEvents visitStatisticsEvents = new VisitStatisticsEvents()
                .setVisitStatistics(user)
                .setEventDate(LocalDateTime.now())
                .setEvents(request.getHeader("events"));

        visitStatisticsRepository.save(user);
        visitStatisticEventRepository.save(visitStatisticsEvents);
    }

}
