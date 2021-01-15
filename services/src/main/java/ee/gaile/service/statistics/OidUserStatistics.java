package ee.gaile.service.statistics;

import ee.gaile.entity.statistics.VisitStatisticUserIp;
import ee.gaile.entity.statistics.VisitStatistics;
import ee.gaile.entity.statistics.VisitStatisticsEvents;
import ee.gaile.repository.statistic.VisitStatisticEventRepository;
import ee.gaile.repository.statistic.VisitStatisticIpRepository;
import ee.gaile.repository.statistic.VisitStatisticVisitDateRepository;
import ee.gaile.repository.statistic.VisitStatisticsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class OidUserStatistics implements Statistics {
    private final VisitStatisticsRepository visitStatisticsRepository;
    private final VisitStatisticIpRepository visitStatisticIpRepository;
    private final VisitStatisticVisitDateRepository visitDateRepository;
    private final UndefinedUserStatistics undefinedUserStatistics;
    private final VisitStatisticEventRepository visitStatisticEventRepository;

    @Override
    public Map<String, String> setUserStatistics(HttpServletRequest request) {
        Map<String, String> response = new HashMap<>();

        Optional<VisitStatistics> visitStatisticsByNameOptional =
                visitStatisticsRepository.findBySessionId(request.getHeader("userId"));

        if (visitStatisticsByNameOptional.isPresent()) {

            VisitStatistics oldUser = visitStatisticsByNameOptional.get();
            oldUser.setLastVisit(LocalDateTime.now())
                    .setLastEvent(LocalDateTime.now())
                    .setTotalVisits(oldUser.getTotalVisits() + 1L);

            if (!request.getHeader("userCity").equals("undefined")) {
                oldUser.setUserCity(request.getHeader("userCity"));
            }
            if (!request.getHeader("userCountry").equals("undefined")) {
                oldUser.setUserLocation(request.getHeader("userCountry"));
            }

            visitStatisticsRepository.save(oldUser);

            if (!request.getHeader("userIP").equals("undefined")) {
                VisitStatisticUserIp oldUserIp = new VisitStatisticUserIp();
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

    public void setEvent(HttpServletRequest request, String sessionId) {
        Optional<VisitStatistics> visitStatisticsByNameOptional =
                visitStatisticsRepository.findBySessionId(sessionId);
        VisitStatistics user = visitStatisticsByNameOptional.orElseThrow(NullPointerException::new);

        if (user.getLastEvent() == null) {
            return;
        }
        long between = Duration.between(user.getLastEvent(), LocalDateTime.now()).toMillis();

        user.setLastEvent(LocalDateTime.now());

        try {
            user.setTotalTimeOnSite(user.getTotalTimeOnSite() + between);
        } catch (NullPointerException e) {
            user.setTotalTimeOnSite(between);
        }

        VisitStatisticsEvents visitStatisticsEvents = new VisitStatisticsEvents()
                .setVisitStatistics(user)
                .setEventDate(LocalDateTime.now())
                .setEvents(request.getHeader("events"));

        visitStatisticsRepository.save(user);
        visitStatisticEventRepository.save(visitStatisticsEvents);
    }
}
