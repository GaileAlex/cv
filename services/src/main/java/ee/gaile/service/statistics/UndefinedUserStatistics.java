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
public class UndefinedUserStatistics implements Statistics {
    private final VisitStatisticsRepository visitStatisticsRepository;
    private final VisitStatisticIpRepository visitStatisticIpRepository;

    @Override
    public void setUserStatistics(HttpServletRequest request) {

        VisitStatistics undefinedUser = new VisitStatistics();
        undefinedUser.setLastVisit(LocalDateTime.now());
        undefinedUser.setTotalVisits(1L);
        undefinedUser.setUsername("undefined");
        undefinedUser.setUserCity("undefined");
        undefinedUser.setUserLocation("undefined");
        undefinedUser.setSessionId(RequestContextHolder.currentRequestAttributes().getSessionId());

        VisitStatisticUserIp undefinedUserIp = new VisitStatisticUserIp();
        undefinedUserIp.setUserIp("undefined");
        undefinedUserIp.setVisitStatistics(undefinedUser);

        visitStatisticsRepository.save(undefinedUser);
        visitStatisticIpRepository.save(undefinedUserIp);

    }

    @Override
    public VisitStatistics getUserTotalTimeOnSite(HttpServletRequest request) {
        Optional<VisitStatistics> visitStatistics = visitStatisticsRepository.findBySessionId(request.getHeader("sessionId"));
        VisitStatistics user = visitStatistics.orElseThrow(NullPointerException::new);

        return setTime(user, request.getHeader("dateOut"));

    }
}
