package ee.gaile.service.statistics;

import ee.gaile.service.email.EmailServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Map;

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
    public Map<String, String> setStatistics(HttpServletRequest request) {

        if (isMailEnable) {
            emailService.sendSimpleMessage(request);
        }

        if (request.getHeader("userId").equals("undefined")) {
            return undefinedUserStatistics.setUserStatistics(request);
        }

        return oldUserStatistics.setUserStatistics(request);
    }

}
