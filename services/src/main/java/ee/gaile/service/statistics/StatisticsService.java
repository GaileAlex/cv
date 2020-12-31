package ee.gaile.service.statistics;

import ee.gaile.entity.statistics.VisitStatisticUserIp;
import ee.gaile.entity.statistics.VisitStatistics;
import ee.gaile.models.statistics.VisitStatisticGraph;
import ee.gaile.models.statistics.VisitStatisticsGraph;
import ee.gaile.repository.statistic.VisitStatisticIpRepository;
import ee.gaile.repository.statistic.VisitStatisticUserRepository;
import ee.gaile.repository.statistic.VisitStatisticsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class StatisticsService {
    private final VisitStatisticsRepository visitStatisticsRepository;
    private final VisitStatisticIpRepository visitStatisticIpRepository;
    private final VisitStatisticUserRepository visitStatisticUserRepository;

    public void setUserStatistics(HttpServletRequest request) {
        String userIP = request.getHeader("userIP");

        Optional<VisitStatistics> visitStatistics =
                visitStatisticsRepository.findByUserIP(userIP);

        if (visitStatistics.isPresent()) {
            visitStatistics.get().setLastVisit(LocalDateTime.now());
            visitStatistics.get().setTotalVisits(visitStatistics.get().getTotalVisits() + 1);

            VisitStatisticUserIp visitStatisticUserIp = new VisitStatisticUserIp();
            visitStatisticUserIp.setUserIp(userIP);
            visitStatisticUserIp.setVisitStatistics(visitStatistics.get());

            visitStatisticIpRepository.save(visitStatisticUserIp);

            if (visitStatistics.get().getUsername() == null || visitStatistics.get().getUsername().equals("undefined")
                    && request.getHeader("user") != null) {
                visitStatistics.get().setUsername(request.getHeader("user"));
            }
        } else {
            VisitStatistics visitStatistic = new VisitStatistics()
                    .setUsername(request.getHeader("user"))
                    .setLastVisit(LocalDateTime.now())
                    .setFirstVisit(LocalDateTime.now())
                    .setTotalVisits(1L)
                    .setUserLocation(request.getHeader("userCountry"))
                    .setUserCity(request.getHeader("userCity"));

            visitStatisticsRepository.save(visitStatistic);

            VisitStatisticUserIp visitStatisticUserIp = new VisitStatisticUserIp();
            visitStatisticUserIp.setUserIp(userIP);
            visitStatisticUserIp.setVisitStatistics(visitStatistic);

            visitStatisticIpRepository.save(visitStatisticUserIp);
        }
    }

    public VisitStatisticGraph getStatisticsGraph(String fromDate, String toDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime fromDateParse = LocalDateTime.parse(fromDate, formatter);
        LocalDateTime toDateParse = LocalDateTime.parse(toDate, formatter);

        List<VisitStatisticsGraph> countedVisitDTOList = visitStatisticUserRepository
                .selectVisitStatistic(fromDateParse, toDateParse);
        List<VisitStatisticsGraph> visitStatisticsNewUsers = visitStatisticUserRepository
                .selectNewVisitors(fromDateParse, toDateParse);

        LocalDate startDate = LocalDate.from(countedVisitDTOList.get(0).getVisitDate());
        LocalDate endDate = LocalDate.from(countedVisitDTOList.get(countedVisitDTOList.size() - 1).getVisitDate());

        List<BigInteger> newUsers = getPointByDate(visitStatisticsNewUsers, startDate, endDate);
        List<BigInteger> totalVisits = getPointByDate(countedVisitDTOList, startDate, endDate);
        List<LocalDate> date = getDates(startDate, endDate);

        return new VisitStatisticGraph(newUsers, totalVisits, date);
    }

    public void setUserTotalTimeOnSite(HttpServletRequest request) {
        Optional<VisitStatistics> visitStatistics = visitStatisticsRepository.findByUserIP(request.getHeader("userIP"));
        VisitStatistics user = visitStatistics.orElseThrow(NullPointerException::new);

        LocalDateTime userEntry = user.getLastVisit();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime userOut = LocalDateTime.parse(request.getHeader("dateOut"), formatter);

        long between = Duration.between(userEntry, userOut).toMillis();

        try {
            user.setTotalTimeOnSite(user.getTotalTimeOnSite() + between);
        } catch (NullPointerException e) {
            user.setTotalTimeOnSite(between);
        }

        visitStatisticsRepository.save(user);


    }

    private List<BigInteger> getPointByDate(List<VisitStatisticsGraph> visitList, LocalDate startDate, LocalDate endDate) {
        List<BigInteger> points = new ArrayList<>();
        Map<LocalDate, BigInteger> map = visitList.stream()
                .collect(Collectors.toMap(VisitStatisticsGraph::getVisitDate, VisitStatisticsGraph::getCountVisits));

        while (!startDate.equals(endDate.plusDays(1))) {
            if (map.get(startDate) != null) {
                points.add(map.get(startDate));
            } else {
                points.add(BigInteger.ZERO);
            }

            startDate = startDate.plusDays(1);
        }

        return points;

    }

    private List<LocalDate> getDates(LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dates = new ArrayList<>();

        while (!startDate.equals(endDate.plusDays(1))) {
            dates.add(startDate);
            startDate = startDate.plusDays(1);
        }

        return dates;
    }

}
