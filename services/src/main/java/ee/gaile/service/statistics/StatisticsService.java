package ee.gaile.service.statistics;

import ee.gaile.entity.statistics.VisitStatistics;
import ee.gaile.models.statistics.VisitStatisticGraph;
import ee.gaile.models.statistics.VisitStatisticsGraph;
import ee.gaile.models.statistics.VisitStatisticsTable;
import ee.gaile.repository.statistic.VisitStatisticsGraphRepository;
import ee.gaile.repository.statistic.VisitStatisticsRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class StatisticsService {
    private final VisitStatisticsRepository visitStatisticsRepository;
    private final VisitStatisticsGraphRepository visitStatisticsGraphRepository;
    private final UndefinedUserStatistics undefinedUserStatistics;
    private final UserWithIpStatistics userWithIpStatistics;
    private final UserWithNameStatistics userWithNameStatistics;

    public Map<String, String> setUserStatistics(HttpServletRequest request) {
        Map<String, String> response = new HashMap<>();
        response.put("sessionId", RequestContextHolder.currentRequestAttributes().getSessionId());

        if (!request.getHeader("user").equals("undefined")) {
            userWithNameStatistics.setUserStatistics(request);
        }
        if (!request.getHeader("userIP").equals("undefined")) {
            userWithIpStatistics.setUserStatistics(request);
        }
        if (request.getHeader("user").equals("undefined") && request.getHeader("userIP").equals("undefined")) {
            undefinedUserStatistics.setUserStatistics(request);
        }

        return response;
    }

    public void setUserTotalTimeOnSite(HttpServletRequest request) {
        VisitStatistics user;

        log.error(request.getHeader("user") + request.getHeader("userIP"));

        if (!request.getHeader("user").equals("undefined")) {
            user = userWithNameStatistics.getUserTotalTimeOnSite(request);
        } else if (!request.getHeader("userIP").equals("undefined")) {
            user = userWithIpStatistics.getUserTotalTimeOnSite(request);
        } else if (request.getHeader("user").equals("undefined") && request.getHeader("userIP").equals("undefined")) {
            user = undefinedUserStatistics.getUserTotalTimeOnSite(request);
        } else {
            throw new NullPointerException();
        }


        visitStatisticsRepository.save(user);

    }

    public VisitStatisticGraph getStatisticsGraph(String fromDate, String toDate,
                                                  Integer pageSize, Integer page) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime fromDateParse = LocalDateTime.parse(fromDate, formatter);
        LocalDateTime toDateParse = LocalDateTime.parse(toDate, formatter);

        List<VisitStatisticsGraph> countedVisitDTOList = visitStatisticsGraphRepository
                .selectVisitStatistic(fromDateParse, toDateParse);
        List<VisitStatisticsGraph> visitStatisticsNewUsers = visitStatisticsGraphRepository
                .selectNewVisitors(fromDateParse, toDateParse);

        List<VisitStatisticsTable> visitStatisticsTable = visitStatisticsGraphRepository
                .findByDate(fromDateParse, toDateParse, pageSize, page * pageSize);

        Long total = visitStatisticsGraphRepository.countTotal(fromDateParse, toDateParse);

        LocalDate startDate = LocalDate.from(countedVisitDTOList.get(0).getVisitDate());
        LocalDate endDate = LocalDate.from(countedVisitDTOList.get(countedVisitDTOList.size() - 1).getVisitDate());

        List<BigInteger> newUsers = getPointByDate(visitStatisticsNewUsers, startDate, endDate);
        List<BigInteger> totalVisits = getPointByDate(countedVisitDTOList, startDate, endDate);
        List<LocalDate> date = getDates(startDate, endDate);

        return new VisitStatisticGraph(newUsers, totalVisits, date, visitStatisticsTable, total);
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
