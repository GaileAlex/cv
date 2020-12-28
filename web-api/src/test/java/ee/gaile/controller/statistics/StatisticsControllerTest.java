package ee.gaile.controller.statistics;

import ee.gaile.CVApplication;
import ee.gaile.service.statistics.StatisticsService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static ee.gaile.service.security.SecurityConfig.API_V1_PREFIX;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CVApplication.class)
@AutoConfigureMockMvc
class StatisticsControllerTest {
    @MockBean
    private StatisticsService statisticsService;

    @Autowired
    private MockMvc mvc;

    @Test
    void getGraphData() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get(API_V1_PREFIX + "/statistic/graph")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void getUserSpy() {
    }
}
