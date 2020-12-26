package ee.gaile.service.statistics;

import ee.gaile.models.statistics.VisitStatisticGraph;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SpringBootTest()
@ActiveProfiles("test")
class StatisticsServiceTest {

    @Autowired
    private StatisticsService statisticsService;

    @Test
    void checkGetStatisticsGraph() {
        VisitStatisticGraph visitStatisticGraph = statisticsService.getStatisticsGraph();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm:ss.SSS");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm");

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(visitStatisticGraph.getVisitStatisticsDTOS().get(0).getUserIP()).isEqualTo(null);
            softly.assertThat(visitStatisticGraph.getVisitStatisticsDTOS().get(0).getUserLocation()).isEqualTo("\"Estonia\"");
            softly.assertThat(visitStatisticGraph.getVisitStatisticsDTOS().get(0)
                    .getFirstVisit()).isEqualTo(LocalDateTime.parse("2020-10-03 08:10:26.709", formatter));
            softly.assertThat(visitStatisticGraph.getVisitStatisticsDTOS().get(0)
                    .getLastVisit()).isEqualTo(LocalDateTime.parse("2020-10-03 14:42:52.521", formatter));
            softly.assertThat(visitStatisticGraph.getVisitStatisticsDTOS().get(0).getTotalVisits()).isEqualTo(4);
            softly.assertThat(visitStatisticGraph.getVisitStatisticsDTOS().get(0).getUsername()).isEqualTo("undefined");

            softly.assertThat(visitStatisticGraph.getCountedVisit().get(0)
                    .getVisitDate()).isEqualTo(LocalDateTime.parse("2020-11-01 00:00", formatter2));
            softly.assertThat(visitStatisticGraph.getCountedVisit().get(0).getCountVisits()).isEqualTo(3);

            softly.assertThat(visitStatisticGraph.getVisitStatisticsNewUsers().get(0)
                    .getVisitDate()).isEqualTo(LocalDateTime.parse("2020-11-01 00:00", formatter2));
            softly.assertThat(visitStatisticGraph.getVisitStatisticsNewUsers().get(0).getCountVisits()).isEqualTo(1);
        });

    }
}
