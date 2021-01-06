package ee.gaile.service.statistics;

import ee.gaile.ApplicationIT;
import ee.gaile.entity.statistics.VisitStatistics;
import ee.gaile.models.statistics.VisitStatisticGraph;
import ee.gaile.repository.statistic.VisitStatisticsRepository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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
        when(request.getHeader("userIP")).thenReturn("2.2.2.2");
        when(request.getHeader("user")).thenReturn("Test");
        when(request.getHeader("userCountry")).thenReturn("Estonia");
        when(request.getHeader("userCity")).thenReturn("Tallinn");
        when(request.getHeader("userId")).thenReturn("undefined");

        statisticsService.setUserStatistics(request);
    }

    @AfterAll
    public void tearDown() {
        visitStatisticsRepository.deleteAll();
    }

    @Test
    void saveNewUser_setUserStatistics() {
        user = visitStatisticsRepository.findByUserIP("2.2.2.2").get();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(user.getUsername()).isEqualTo("undefined");
            softly.assertThat(user.getTotalVisits()).isEqualTo(1);
            softly.assertThat(user.getVisitStatisticVisitDates().size()).isEqualTo(1);
        });
    }

    @Test
    void saveOldUser_setUserStatistics() {
        user = visitStatisticsRepository.findByUserIP("2.2.2.2").get();
        when(request.getHeader("userId")).thenReturn(user.getSessionId());

        statisticsService.setUserStatistics(request);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(user.getUsername()).isEqualTo("undefined");
            softly.assertThat(user.getTotalVisits()).isEqualTo(2);
            softly.assertThat(user.getVisitStatisticVisitDates().size()).isEqualTo(2);
        });
    }

    @Test
    void checkGetStatisticsGraph() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String from = LocalDateTime.parse(LocalDateTime.now().minusDays(1).format(formatter)).toString();
        String to = LocalDateTime.parse(LocalDateTime.now().plusDays(1).format(formatter)).toString();
        Integer page = 0;
        Integer pageSize = 8;

        VisitStatisticGraph visitStatisticGraph = statisticsService.getStatisticsGraph(from, to,
                pageSize, page * pageSize);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(visitStatisticGraph.getDates().get(0)).isEqualTo(LocalDate.now());
            softly.assertThat(visitStatisticGraph.getTotalVisits().get(0)).isEqualTo(1);
        });
    }
}
