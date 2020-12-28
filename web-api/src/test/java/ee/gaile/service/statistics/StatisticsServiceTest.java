package ee.gaile.service.statistics;

import ee.gaile.CVApplication;
import ee.gaile.models.statistics.VisitStatisticGraph;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CVApplication.class)
class StatisticsServiceTest {

    @Autowired
    private StatisticsService statisticsService;

    @Test
    void checkGetStatisticsGraph() {
        VisitStatisticGraph visitStatisticGraph = statisticsService.getStatisticsGraph();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(visitStatisticGraph.getDates().get(0)).isEqualTo("2020-11-01");
            softly.assertThat(visitStatisticGraph.getNewUsers().get(0)).isEqualTo(1);
            softly.assertThat(visitStatisticGraph.getTotalVisits().get(0)).isEqualTo(3);
        });

    }
}
