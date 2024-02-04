package ee.gaile.service.statistics.impl;

import ee.gaile.entity.statistics.VisitStatisticUserIpEntity;
import ee.gaile.entity.statistics.VisitStatisticsEntity;
import ee.gaile.repository.statistic.VisitStatisticIpRepository;
import ee.gaile.repository.statistic.VisitStatisticVisitDateRepository;
import ee.gaile.repository.statistic.VisitStatisticsRepository;
import ee.gaile.service.statistics.Statistics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Service for saving statistics of visits to the site by a old user
 *
 * @author Aleksei Gaile
 */
@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OldUserStatistics implements Statistics {
    private static final String UNDEFINED = "undefined";
    private final VisitStatisticsRepository visitStatisticsRepository;
    private final VisitStatisticIpRepository visitStatisticIpRepository;
    private final VisitStatisticVisitDateRepository visitDateRepository;
    private final UndefinedUserStatistics undefinedUserStatistics;

    @Override
    public Map<String, String> setUserStatistics(HttpServletRequest request) {
        Map<String, String> response = new HashMap<>();

        Optional<VisitStatisticsEntity> visitStatisticsByNameOptional =
                visitStatisticsRepository.findBySessionId(request.getHeader("userId"));

        if (visitStatisticsByNameOptional.isPresent()) {

            VisitStatisticsEntity oldUser = visitStatisticsByNameOptional.get();
            oldUser.setLastVisit(LocalDateTime.now())
                    .setLastEvent(LocalDateTime.now())
                    .setTotalVisits(oldUser.getTotalVisits() + 1L);

            if (!UNDEFINED.equals(request.getHeader("userCity"))) {
                oldUser.setUserCity(request.getHeader("userCity"));
            }
            if (!UNDEFINED.equals(request.getHeader("userCountry"))) {
                oldUser.setUserLocation(request.getHeader("userCountry"));
            }

            visitStatisticsRepository.save(oldUser);

            if (!UNDEFINED.equals(request.getHeader("userIP"))) {
                VisitStatisticUserIpEntity oldUserIp = new VisitStatisticUserIpEntity();
                oldUserIp.setUserIp(request.getHeader("userIP"));
                oldUserIp.setVisitStatistics(oldUser);
                visitStatisticIpRepository.save(oldUserIp);
            }

            setVisitDate(oldUser, visitDateRepository);

            response.put("sessionId", oldUser.getSessionId());
            return response;
        }

        return undefinedUserStatistics.setUserStatistics(request);
    }

}
