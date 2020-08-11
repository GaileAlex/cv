package ee.gaile.service.statistics;

import ee.gaile.entity.users.VisitStatistics;
import ee.gaile.repository.VisitStatisticsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class StatisticsService {
    private final VisitStatisticsRepository visitStatisticsRepository;

    public void setUserStatistics(HttpServletRequest request) {
        Optional<VisitStatistics> visitStatistics =
                visitStatisticsRepository.findByUserIP(request.getHeader("userIP"));

        if (visitStatistics.isPresent()) {
            visitStatistics.get().setLastVisit(new Date());
            visitStatistics.get().setTotalVisits(visitStatistics.get().getTotalVisits() + 1);
            if (visitStatistics.get().getUsername() == null || visitStatistics.get().getUsername().equals("undefined")
                    && request.getHeader("user") != null) {
                visitStatistics.get().setUsername(request.getHeader("user"));
            }
        } else {
            VisitStatistics visitStatistic = VisitStatistics.builder()
                    .username(request.getHeader("user"))
                    .lastVisit(new Date())
                    .firstVisit(new Date())
                    .totalVisits(1L)
                    .userIP(request.getHeader("userIP"))
                    .userLocation(request.getHeader("userCountry"))
                    .build();

            visitStatisticsRepository.save(visitStatistic);
        }
    }

    public List<VisitStatistics> getStatisticsGraph() {
        return visitStatisticsRepository.findAll();
    }
}
