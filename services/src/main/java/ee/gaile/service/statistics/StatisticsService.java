package ee.gaile.service.statistics;

import ee.gaile.models.statistics.VisitStatisticGraph;

/**
 * @author Aleksei Gaile 19-Sep-21
 */
public interface StatisticsService {

    /**
     * Gives data for the graph of site visit statistics
     *
     * @param fromDate - graph start date
     * @param toDate   - graph end date
     * @param pageSize - number of pages in the table
     * @param page     - page number
     * @return - data for the graph of site visit statistics
     */
    VisitStatisticGraph getStatisticsGraph(String fromDate, String toDate,
                                           Integer pageSize, Integer page);
}
