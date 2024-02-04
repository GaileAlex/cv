package ee.gaile.service.statistics.impl;

import ee.gaile.entity.statistics.VisitStatisticUserIpEntity;
import ee.gaile.entity.statistics.VisitStatisticsEntity;
import ee.gaile.repository.statistic.VisitStatisticIpRepository;
import ee.gaile.repository.statistic.VisitStatisticVisitDateRepository;
import ee.gaile.repository.statistic.VisitStatisticsRepository;
import ee.gaile.service.statistics.Statistics;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Service for saving statistics of visits to the site by a new user
 *
 * @author Aleksei Gaile
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UndefinedUserStatistics implements Statistics {
    private final VisitStatisticsRepository visitStatisticsRepository;
    private final VisitStatisticIpRepository visitStatisticIpRepository;
    private final VisitStatisticVisitDateRepository visitDateRepository;

    @Override
    public Map<String, String> setUserStatistics(HttpServletRequest request) {
        Map<String, String> response = new HashMap<>();
        String sessionId = UUID.randomUUID().toString();
        response.put("sessionId", sessionId);

        VisitStatisticsEntity undefinedUser = new VisitStatisticsEntity()
                .setFirstVisit(LocalDateTime.now())
                .setLastVisit(LocalDateTime.now())
                .setLastEvent(LocalDateTime.now())
                .setTotalVisits(1L)
                .setUsername("undefined")
                .setUserCity(request.getHeader("userCity"))
                .setUserLocation(request.getHeader("userCountry"))
                .setSessionId(sessionId);

        VisitStatisticUserIpEntity undefinedUserIp = new VisitStatisticUserIpEntity();
        undefinedUserIp.setUserIp(request.getHeader("userIP"));
        undefinedUserIp.setVisitStatistics(undefinedUser);

        visitStatisticsRepository.save(undefinedUser);
        visitStatisticIpRepository.save(undefinedUserIp);
        setVisitDate(undefinedUser, visitDateRepository);

        return response;
    }

}
