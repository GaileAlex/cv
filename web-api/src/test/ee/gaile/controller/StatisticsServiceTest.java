package ee.gaile.controller;

import ee.gaile.models.statistics.VisitStatisticGraph;
import ee.gaile.service.statistics.StatisticsService;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.sql.Timestamp;

@SpringBootTest()
@ActiveProfiles("test")
class StatisticsServiceTest {

    @Autowired
    private StatisticsService statisticsService;

    @Test
    void checkGetStatisticsGraph() {
        VisitStatisticGraph visitStatisticGraph = statisticsService.getStatisticsGraph();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(visitStatisticGraph.getVisitStatisticsDTOS().get(0).getUserIP()).isEqualTo(null);
            softly.assertThat(visitStatisticGraph.getVisitStatisticsDTOS().get(0).getUserLocation()).isEqualTo("\"Estonia\"");
            softly.assertThat(visitStatisticGraph.getVisitStatisticsDTOS().get(0)
                    .getFirstVisit()).isEqualTo(Timestamp.valueOf("2020-10-03 08:10:26.709"));
            softly.assertThat(visitStatisticGraph.getVisitStatisticsDTOS().get(0)
                    .getLastVisit()).isEqualTo(Timestamp.valueOf("2020-10-03 14:42:52.521"));
            softly.assertThat(visitStatisticGraph.getVisitStatisticsDTOS().get(0).getTotalVisits()).isEqualTo(4);
            softly.assertThat(visitStatisticGraph.getVisitStatisticsDTOS().get(0).getUsername()).isEqualTo("undefined");

            softly.assertThat(visitStatisticGraph.getCountedVisit().get(0)
                    .getVisitDate()).isEqualTo(Timestamp.valueOf("2020-11-01 00:00:00.0"));
            softly.assertThat(visitStatisticGraph.getCountedVisit().get(0).getCountVisits()).isEqualTo(3);

            softly.assertThat(visitStatisticGraph.getVisitStatisticsNewUsers().get(0)
                    .getVisitDate()).isEqualTo(Timestamp.valueOf("2020-11-01 00:00:00.0"));
            softly.assertThat(visitStatisticGraph.getVisitStatisticsNewUsers().get(0).getCountVisits()).isEqualTo(1);
        });

    }
}
