package ee.gaile.service.statistics;

import ee.gaile.entity.statistics.VisitStatisticUserIp;
import ee.gaile.entity.statistics.VisitStatistics;
import ee.gaile.repository.statistic.VisitStatisticIpRepository;
import ee.gaile.repository.statistic.VisitStatisticVisitDateRepository;
import ee.gaile.repository.statistic.VisitStatisticsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class UserWithIpStatistics implements Statistics {
    private final VisitStatisticsRepository visitStatisticsRepository;
    private final VisitStatisticIpRepository visitStatisticIpRepository;
    private final VisitStatisticVisitDateRepository visitDateRepository;

    @Override
    public void setUserStatistics(HttpServletRequest request) {
        Optional<VisitStatistics> visitStatisticsOptional =
                visitStatisticsRepository.findByUserIP(request.getHeader("userIP"));

        if (visitStatisticsOptional.isPresent()) {
            VisitStatistics visitStatistic = visitStatisticsOptional.get();
            visitStatistic.setLastVisit(LocalDateTime.now());
            visitStatistic.setTotalVisits(visitStatistic.getTotalVisits() + 1);
            visitStatistic.setUserCity(request.getHeader("userCity"));
            visitStatistic.setUserLocation(request.getHeader("userCountry"));
            visitStatistic.setSessionId(RequestContextHolder.currentRequestAttributes().getSessionId());

            visitStatisticsRepository.save(visitStatistic);
            setVisitDate(visitStatistic, visitDateRepository);

        } else {
            VisitStatistics visitStatistic = new VisitStatistics()
                    .setUsername(request.getHeader("user"))
                    .setLastVisit(LocalDateTime.now())
                    .setFirstVisit(LocalDateTime.now())
                    .setTotalVisits(1L)
                    .setSessionId(RequestContextHolder.currentRequestAttributes().getSessionId())
                    .setUserLocation(request.getHeader("userCountry"))
                    .setUserCity(request.getHeader("userCity"));

            VisitStatisticUserIp visitStatisticUserIp = new VisitStatisticUserIp();
            visitStatisticUserIp.setUserIp(request.getHeader("userIP"));
            visitStatisticUserIp.setVisitStatistics(visitStatistic);

            visitStatisticsRepository.save(visitStatistic);
            visitStatisticIpRepository.save(visitStatisticUserIp);

            setVisitDate(visitStatistic, visitDateRepository);
        }
    }

    @Override
    public VisitStatistics getUserTotalTimeOnSite(HttpServletRequest request) {
        Optional<VisitStatistics> visitStatistics = visitStatisticsRepository.findByUserIP(request.getHeader("userIP"));
        VisitStatistics user = visitStatistics.orElseThrow(NullPointerException::new);

        return setTime(user, request.getHeader("dateOut"));
    }
}
