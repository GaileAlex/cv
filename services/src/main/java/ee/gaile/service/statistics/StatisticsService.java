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

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class StatisticsService {
    private final VisitStatisticsRepository visitStatisticsRepository;
    private final VisitStatisticsGraphRepository visitStatisticsGraphRepository;
    private final UndefinedUserStatistics undefinedUserStatistics;
    private final OidUserStatistics oidUserStatistics;

    public Map<String, String> setUserStatistics(HttpServletRequest request) {

        if (request.getHeader("userId").equals("undefined")) {
            return undefinedUserStatistics.setUserStatistics(request);
        }

        return oidUserStatistics.setUserStatistics(request);
    }

    public void setUserEvent(HttpServletRequest request) {
        String sessionId;
        if (!request.getHeader("userId").equals("undefined")) {
            sessionId = request.getHeader("userId");
        } else if (!request.getHeader("sessionStorageUserId").equals("undefined")) {
            sessionId = request.getHeader("userId");
        } else {
            return;
        }

        oidUserStatistics.setEvent(request, sessionId);
    }

    public void setUserTotalTimeOnSite(HttpServletRequest request) {
        Optional<VisitStatistics> visitStatistics =
                visitStatisticsRepository.findBySessionId(request.getHeader("userId"));

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

    }

    public VisitStatisticGraph getStatisticsGraph(String fromDate, String toDate,
                                                  Integer pageSize, Integer page) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime fromDateParse = LocalDateTime.parse(fromDate, formatter);
        LocalDateTime toDateParse = LocalDateTime.parse(toDate, formatter).plusDays(1);

        List<VisitStatisticsGraph> countedVisitDTOList = visitStatisticsGraphRepository
                .selectVisitStatistic(fromDateParse, toDateParse);
        List<VisitStatisticsGraph> visitStatisticsNewUsers = visitStatisticsGraphRepository
                .selectNewVisitors(fromDateParse, toDateParse);

        List<VisitStatisticsTable> visitStatisticsTable = visitStatisticsGraphRepository
                .findByDate(fromDateParse, toDateParse, pageSize, page * pageSize);

        Long total = visitStatisticsGraphRepository.countTotal(fromDateParse, toDateParse);

        LocalDate from;

        try {
            from = countedVisitDTOList.get(0).getVisitDate();
        } catch (IndexOutOfBoundsException e) {
            return new VisitStatisticGraph();
        }

        LocalDate startDate = LocalDate.from(from);
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
