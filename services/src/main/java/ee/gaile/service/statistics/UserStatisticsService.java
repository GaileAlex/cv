package ee.gaile.service.statistics;

import ee.gaile.entity.statistics.VisitStatistics;
import ee.gaile.repository.statistic.VisitStatisticsRepository;
import ee.gaile.service.email.EmailServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

/**
 * Service for working with statistics of site visits
 *
 * @author Aleksei Gaile
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserStatisticsService {
    private final VisitStatisticsRepository visitStatisticsRepository;
    private final UndefinedUserStatistics undefinedUserStatistics;
    private final OldUserStatistics oldUserStatistics;
    private final EmailServiceImpl emailService;

    @Value("${mail.enable}")
    private boolean isMailEnable;

    /**
     * Definition of new or old user
     *
     * @param request - HttpServletRequest
     * @return - sessionId
     */
    public Map<String, String> setUserStatistics(HttpServletRequest request) {

        if (isMailEnable) {
            emailService.sendSimpleMessage(request.getHeader("userCountry"));
        }

        if (request.getHeader("userId").equals("undefined")) {
            return undefinedUserStatistics.setUserStatistics(request);
        }

        return oldUserStatistics.setUserStatistics(request);
    }

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

        oldUserStatistics.setEvent(request, sessionId);
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
}
