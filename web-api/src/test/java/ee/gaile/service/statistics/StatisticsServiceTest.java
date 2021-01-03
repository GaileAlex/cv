package ee.gaile.service.statistics;

import ee.gaile.ApplicationIT;
import ee.gaile.entity.statistics.VisitStatistics;
import ee.gaile.models.statistics.VisitStatisticGraph;
import ee.gaile.repository.statistic.VisitStatisticsRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StatisticsServiceTest extends ApplicationIT {

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private VisitStatisticsRepository visitStatisticsRepository;

    @Test
    void saveNewUserToRepo_setUserStatistics() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader("userIP")).thenReturn("222.222.222.222");
        when(request.getHeader("user")).thenReturn("Test");
        when(request.getHeader("userCountry")).thenReturn("Estonia");
        when(request.getHeader("userCity")).thenReturn("Tallinn");

        statisticsService.setUserStatistics(request);

        Optional<VisitStatistics> user = visitStatisticsRepository.findByUserIP("222.222.222.222");


        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(user.get().getUsername()).isEqualTo("Test");
            softly.assertThat(user.get().getUserLocation()).isEqualTo("Estonia");
            softly.assertThat(user.get().getUserCity()).isEqualTo("Tallinn");
        });

        visitStatisticsRepository.delete(user.get());
    }

    @Test
    void saveOldUserToRepo_setUserStatistics() {
        HttpServletRequest request = mock(HttpServletRequest.class);

        when(request.getHeader("userIP")).thenReturn("555.555.555.555");
        when(request.getHeader("user")).thenReturn("Test-old");
        when(request.getHeader("userCountry")).thenReturn("Estonia");
        when(request.getHeader("userCity")).thenReturn("Tallinn");

        statisticsService.setUserStatistics(request);
        statisticsService.setUserStatistics(request);

        Optional<VisitStatistics> user = visitStatisticsRepository.findByUserIP("555.555.555.555");

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(user.get().getUsername()).isEqualTo("Test-old");
            softly.assertThat(user.get().getUserLocation()).isEqualTo("Estonia");
            softly.assertThat(user.get().getUserCity()).isEqualTo("Tallinn");
        });

        visitStatisticsRepository.delete(user.get());
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
            softly.assertThat(visitStatisticGraph.getNewUsers().get(0)).isEqualTo(1);
            softly.assertThat(visitStatisticGraph.getTotalVisits().get(0)).isEqualTo(1);
        });
    }
}
