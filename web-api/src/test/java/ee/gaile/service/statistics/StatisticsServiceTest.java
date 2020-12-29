package ee.gaile.service.statistics;

import ee.gaile.ApplicationIT;
import ee.gaile.models.statistics.VisitStatisticGraph;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class StatisticsServiceTest extends ApplicationIT {

    @Autowired
    private StatisticsService statisticsService;

    @Test
    void checkGetStatisticsGraph() {
        String fromDate = "2020-11-01T00:00:00";
        String toDate = "2020-12-31T00:00:00";
        VisitStatisticGraph visitStatisticGraph = statisticsService.getStatisticsGraph(fromDate, toDate);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(visitStatisticGraph.getDates().get(0)).isEqualTo("2020-12-29");
            softly.assertThat(visitStatisticGraph.getNewUsers().get(0)).isEqualTo(0);
            softly.assertThat(visitStatisticGraph.getTotalVisits().get(0)).isEqualTo(1);
        });

    }
}
