package ee.gaile.service.statistics;

import ee.gaile.entity.statistics.VisitStatisticsEntity;
import ee.gaile.entity.statistics.VisitStatisticsEventsEntity;
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
public class UserEventsImpl implements UserEvents {
    private static final String USER_ID = "userId";

    private final VisitStatisticsRepository visitStatisticsRepository;
    private final VisitStatisticEventRepository visitStatisticEventRepository;

    @Override
    public void setUserEvent(HttpServletRequest request) {
        String sessionId;
        if (!request.getHeader(USER_ID).equals("undefined")) {
            sessionId = request.getHeader(USER_ID);
        } else if (!request.getHeader("sessionStorageUserId").equals("undefined")) {
            sessionId = request.getHeader(USER_ID);
        } else {
            return;
        }

        setEvent(request, sessionId);
    }

    @Override
    public void setUserTotalTimeOnSite(HttpServletRequest request) {
        Optional<VisitStatisticsEntity> visitStatistics =
                visitStatisticsRepository.findBySessionId(request.getHeader(USER_ID));

        VisitStatisticsEntity user = visitStatistics.orElseThrow(NullPointerException::new);

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
    private void setEvent(HttpServletRequest request, String sessionId) {
        Optional<VisitStatisticsEntity> visitStatisticsByNameOptional =
                visitStatisticsRepository.findBySessionId(sessionId);
        VisitStatisticsEntity user = visitStatisticsByNameOptional.orElseThrow(NullPointerException::new);

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

        VisitStatisticsEventsEntity visitStatisticsEvents = new VisitStatisticsEventsEntity()
                .setVisitStatistics(user)
                .setEventDate(LocalDateTime.now())
                .setEvents(request.getHeader("events"));

        visitStatisticsRepository.save(user);
        visitStatisticEventRepository.save(visitStatisticsEvents);
    }

}
