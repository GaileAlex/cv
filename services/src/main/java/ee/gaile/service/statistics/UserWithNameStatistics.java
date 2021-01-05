package ee.gaile.service.statistics;

import ee.gaile.entity.statistics.VisitStatisticUserIp;
import ee.gaile.entity.statistics.VisitStatistics;
import ee.gaile.repository.statistic.VisitStatisticIpRepository;
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
public class UserWithNameStatistics implements Statistics {
    private final VisitStatisticsRepository visitStatisticsRepository;
    private final VisitStatisticIpRepository visitStatisticIpRepository;

    @Override
    public void setUserStatistics(HttpServletRequest request) {
        Optional<VisitStatistics> visitStatisticsByNameOptional =
                visitStatisticsRepository.findByUserName(request.getHeader("user"));
        Optional<VisitStatistics> visitStatisticsByIpOptional =
                visitStatisticsRepository.findByUserName(request.getHeader("userIP"));

        if (visitStatisticsByNameOptional.isPresent()) {
            VisitStatistics visitStatisticsByName = visitStatisticsByNameOptional.get();

            if (visitStatisticsByIpOptional.isPresent() && visitStatisticsByIpOptional.get().getSessionId()
                    .equals(RequestContextHolder.currentRequestAttributes().getSessionId())) {
                visitStatisticsByIpOptional.get().setUsername(request.getHeader("user"));

                visitStatisticsRepository.save(visitStatisticsByIpOptional.get());
            } else {
                visitStatisticsByName.setTotalVisits(visitStatisticsByName.getTotalVisits() + 1);
                visitStatisticsByName.setUserCity(request.getHeader("userCity"));
                visitStatisticsByName.setUserLocation(request.getHeader("userCountry"));
                visitStatisticsByName.setLastVisit(LocalDateTime.now());
                visitStatisticsByName.setFirstVisit(LocalDateTime.now());

                VisitStatisticUserIp visitStatisticUserIp = new VisitStatisticUserIp();
                visitStatisticUserIp.setUserIp(request.getHeader("userIP"));
                visitStatisticUserIp.setVisitStatistics(visitStatisticsByName);

                visitStatisticsRepository.save(visitStatisticsByName);
                visitStatisticIpRepository.save(visitStatisticUserIp);
            }

        } else {
            VisitStatistics visitStatistic = new VisitStatistics()
                    .setUsername(request.getHeader("user"))
                    .setLastVisit(LocalDateTime.now())
                    .setFirstVisit(LocalDateTime.now())
                    .setTotalVisits(1L)
                    .setUserLocation(request.getHeader("userCountry"))
                    .setUserCity(request.getHeader("userCity"));

            VisitStatisticUserIp visitStatisticUserIp = new VisitStatisticUserIp();
            visitStatisticUserIp.setUserIp(request.getHeader("userIP"));
            visitStatisticUserIp.setVisitStatistics(visitStatistic);

            visitStatisticsRepository.save(visitStatistic);
            visitStatisticIpRepository.save(visitStatisticUserIp);
        }
    }

    @Override
    public VisitStatistics getUserTotalTimeOnSite(HttpServletRequest request) {
        Optional<VisitStatistics> visitStatistics = visitStatisticsRepository.findByUserName(request.getHeader("user"));
        VisitStatistics user = visitStatistics.orElseThrow(NullPointerException::new);

        return setTime(user, request.getHeader("dateOut"));
    }
}
