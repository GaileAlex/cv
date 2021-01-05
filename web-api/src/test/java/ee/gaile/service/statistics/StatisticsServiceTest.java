package ee.gaile.service.statistics;

import ee.gaile.ApplicationIT;
import ee.gaile.entity.statistics.VisitStatistics;
import ee.gaile.models.statistics.VisitStatisticGraph;
import ee.gaile.repository.statistic.VisitStatisticsRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StatisticsServiceTest extends ApplicationIT {

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private VisitStatisticsRepository visitStatisticsRepository;

    HttpServletRequest request;
    VisitStatistics user;

    @BeforeEach
    void setUp() {
        request = mock(HttpServletRequest.class);
        when(request.getHeader("userIP")).thenReturn("111.111.111.111");
        when(request.getHeader("user")).thenReturn("Test");
        when(request.getHeader("userCountry")).thenReturn("Estonia");
        when(request.getHeader("userCity")).thenReturn("Tallinn");

        user = visitStatisticsRepository.findByUserIP("111.111.111.111").get();

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void saveNewUserWithIp_setUserStatistics() {
        statisticsService.setUserStatistics(request);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(user.getUsername()).isEqualTo("Test");
            softly.assertThat(user.getUserLocation()).isEqualTo("Estonia");
            softly.assertThat(user.getUserCity()).isEqualTo("Tallinn");
        });
    }

    @Test
    void saveOldUserWithIp_setUserStatistics() {
        statisticsService.setUserStatistics(request);
        statisticsService.setUserStatistics(request);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(user.getUsername()).isEqualTo("Test");
            softly.assertThat(user.getUserLocation()).isEqualTo("Estonia");
            softly.assertThat(user.getUserCity()).isEqualTo("Tallinn");
        });

    }

    @Test
    void saveNewUserWithoutIp_setUserStatistics() {

        statisticsService.setUserStatistics(request);
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(user.getUsername()).isEqualTo("Test");
            softly.assertThat(user.getUserLocation()).isEqualTo("Estonia");
            softly.assertThat(user.getUserCity()).isEqualTo("Tallinn");
        });
    }

    @Test
    void saveOldUserWithoutIp_setUserStatistics() {

        statisticsService.setUserStatistics(request);
        statisticsService.setUserStatistics(request);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(user.getUsername()).isEqualTo("Test");
            softly.assertThat(user.getUserLocation()).isEqualTo("Estonia");
            softly.assertThat(user.getUserCity()).isEqualTo("Tallinn");
        });

    }

    @Test
    void checkGetStatisticsGraph() {
        String fromDate = "2020-11-01T00:00:00";
        String toDate = "2020-12-30T00:00:00";
        Integer page = 0;
        Integer pageSize = 8;
        VisitStatisticGraph visitStatisticGraph = statisticsService.getStatisticsGraph(fromDate, toDate,
                pageSize, page * pageSize);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(visitStatisticGraph.getDates().get(0)).isEqualTo("2020-12-07");
            softly.assertThat(visitStatisticGraph.getTotalVisits().get(0)).isEqualTo(1);
        });
    }
}
