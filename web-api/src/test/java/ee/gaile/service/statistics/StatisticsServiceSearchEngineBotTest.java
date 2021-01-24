package ee.gaile.service.statistics;

import ee.gaile.ApplicationIT;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StatisticsServiceSearchEngineBotTest extends ApplicationIT {

    @Autowired
    private StatisticsService statisticsService;

    @Test
    void googleBoot_setUserStatistics() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("userIP")).thenReturn("66.249.73.177");
        Map<String, String> map = statisticsService.setUserStatistics(request);

        SoftAssertions.assertSoftly(softly -> softly.assertThat(map).isEqualTo(Collections.emptyMap()));
    }
}
