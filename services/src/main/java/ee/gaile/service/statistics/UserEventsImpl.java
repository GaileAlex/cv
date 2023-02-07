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
    private static final long FIVE_MINUTE = 300000L;

    private final VisitStatisticsRepository visitStatisticsRepository;
    private final VisitStatisticEventRepository visitStatisticEventRepository;
    private final UserStatisticsServiceImpl userStatisticsService;

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

        VisitStatisticsEntity user = visitStatistics.orElse(new VisitStatisticsEntity());

        LocalDateTime userEntry = user.getLastVisit();
        LocalDateTime userOut = LocalDateTime.now();

        try {
            long between = Duration.between(userEntry, userOut).toMillis();
            user.setTotalTimeOnSite(user.getTotalTimeOnSite() + between);
        } catch (NullPointerException e) {
            log.warn("userEntry is null");
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
        VisitStatisticsEntity user = visitStatisticsByNameOptional.orElse(null);

        if (user == null || user.getLastEvent() == null) {
            return;
        }
        long between = Duration.between(user.getLastEvent(), LocalDateTime.now()).toMillis();

        if (between > FIVE_MINUTE) {
            userStatisticsService.setStatistics(request);
            return;
        }

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
