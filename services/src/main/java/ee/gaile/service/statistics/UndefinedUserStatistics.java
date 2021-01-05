package ee.gaile.service.statistics;

import ee.gaile.entity.statistics.VisitStatisticUserIp;
import ee.gaile.entity.statistics.VisitStatistics;
import ee.gaile.repository.statistic.VisitStatisticIpRepository;
import ee.gaile.repository.statistic.VisitStatisticVisitDateRepository;
import ee.gaile.repository.statistic.VisitStatisticsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class UndefinedUserStatistics implements Statistics {
    private final VisitStatisticsRepository visitStatisticsRepository;
    private final VisitStatisticIpRepository visitStatisticIpRepository;
    private final VisitStatisticVisitDateRepository visitDateRepository;

    @Override
    public Map<String, String> setUserStatistics(HttpServletRequest request) {
        Map<String, String> response = new HashMap<>();
        String sessionId = UUID.randomUUID().toString();
        response.put("sessionId", sessionId);

        VisitStatistics undefinedUser = new VisitStatistics()
                .setFirstVisit(LocalDateTime.now())
                .setLastVisit(LocalDateTime.now())
                .setTotalVisits(1L)
                .setUsername("undefined")
                .setUserCity(request.getHeader("userCity"))
                .setUserLocation(request.getHeader("userCountry"))
                .setSessionId(sessionId);

        VisitStatisticUserIp undefinedUserIp = new VisitStatisticUserIp();
        undefinedUserIp.setUserIp(request.getHeader("userIP"));
        undefinedUserIp.setVisitStatistics(undefinedUser);

        visitStatisticsRepository.save(undefinedUser);
        visitStatisticIpRepository.save(undefinedUserIp);
        setVisitDate(undefinedUser, visitDateRepository);

        return response;
    }

}
