package ee.gaile.service.statistics.impl;

import ee.gaile.service.email.EmailService;
import ee.gaile.service.statistics.UserStatisticsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class UserStatisticsServiceImpl implements UserStatisticsService {
    private final UndefinedUserStatistics undefinedUserStatistics;
    private final OldUserStatistics oldUserStatistics;
    private final EmailService emailService;

    @Value("${mail.enable}")
    private boolean isMailEnable;

    @Override
    public Map<String, String> setStatistics(HttpServletRequest request) {

        if (isMailEnable) {
            emailService.sendSimpleMessage(request);
        }

        if (request.getHeader("userId") != null && request.getHeader("userId").equals("undefined")) {
            return undefinedUserStatistics.setUserStatistics(request);
        }

        return oldUserStatistics.setUserStatistics(request);
    }

}
