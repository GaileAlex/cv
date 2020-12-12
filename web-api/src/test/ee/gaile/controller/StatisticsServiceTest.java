package ee.gaile.controller;

import ee.gaile.models.statistics.VisitStatisticGraph;
import ee.gaile.service.statistics.StatisticsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest()
@ActiveProfiles("dev")
class StatisticsServiceTest {

    @Autowired
    private StatisticsService statisticsService;

    @Test
    void checkStatisticsService() {
        VisitStatisticGraph visitStatisticGraph = statisticsService.getStatisticsGraph();

    }
}
