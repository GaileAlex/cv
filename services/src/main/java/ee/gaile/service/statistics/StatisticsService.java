package ee.gaile.service.statistics;

import ee.gaile.models.statistics.VisitStatisticGraph;
import ee.gaile.models.statistics.VisitStatisticsGraph;
import ee.gaile.models.statistics.VisitStatisticsTable;
import ee.gaile.repository.statistic.VisitStatisticsGraphRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service data for user statistics table
 *
 * @author Aleksei Gaile
 */
@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class StatisticsService {
    private final VisitStatisticsGraphRepository visitStatisticsGraphRepository;

    /**
     * Gives data for the graph of site visit statistics
     *
     * @param fromDate - graph start date
     * @param toDate   - graph end date
     * @param pageSize - number of pages in the table
     * @param page     - page number
     * @return - data for the graph of site visit statistics
     */
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

    /**
     * Generates graph points according to the maximum date range
     *
     * @param visitList - list of user visits
     * @param fromDate  - graph start date
     * @param toDate    - graph end date
     * @return - list of points user visits
     */
    private List<BigInteger> getPointByDate(List<VisitStatisticsGraph> visitList, LocalDate fromDate, LocalDate toDate) {
        List<BigInteger> points = new ArrayList<>();
        Map<LocalDate, BigInteger> map = visitList.stream()
                .collect(Collectors.toMap(VisitStatisticsGraph::getVisitDate, VisitStatisticsGraph::getCountVisits));

        while (!fromDate.equals(toDate.plusDays(1))) {
            if (map.get(fromDate) != null) {
                points.add(map.get(fromDate));
            } else {
                points.add(BigInteger.ZERO);
            }

            fromDate = fromDate.plusDays(1);
        }

        return points;

    }

    /**
     * Forms the points of the chart dates according to the interval
     *
     * @param fromDate - graph start date
     * @param toDate   - graph end date
     * @return - list date for graph
     */
    private List<LocalDate> getDates(LocalDate fromDate, LocalDate toDate) {
        List<LocalDate> dates = new ArrayList<>();

        while (!fromDate.equals(toDate.plusDays(1))) {
            dates.add(fromDate);
            fromDate = fromDate.plusDays(1);
        }

        return dates;
    }

}
