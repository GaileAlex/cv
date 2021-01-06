package ee.gaile.controller.statistics;

import ee.gaile.ApplicationIT;
import ee.gaile.service.statistics.StatisticsService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import javax.servlet.http.HttpServletRequest;

import static ee.gaile.UtilsTests.asJsonString;
import static ee.gaile.service.security.SecurityConfig.API_V1_PREFIX;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class StatisticsControllerTest extends ApplicationIT {
    @MockBean
    private StatisticsService statisticsService;

    @Autowired
    private MockMvc mvc;

    @Test
    void checkUserPermissions_GetGraphData() throws Exception {
        String fromDate = "2020-12-01T00:00:00";
        String toDate = "2021-01-03T14:26:58";
        Integer page = 0;
        Integer pageSize = 8;
        String url = API_V1_PREFIX + "/statistic/graph/fromDate/{fromDate}/toDate/{toDate}/pageSize/{pageSize}/page/{page}";

        mvc.perform(MockMvcRequestBuilders
                .get(url, fromDate, toDate, pageSize, page)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void checkSetUserSpy() throws Exception {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        mvc.perform(MockMvcRequestBuilders
                .post(API_V1_PREFIX + "/statistic/user")
                .content(asJsonString(req))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    void checkSetUserOut() throws Exception {
        HttpServletRequest req = Mockito.mock(HttpServletRequest.class);
        mvc.perform(MockMvcRequestBuilders
                .post(API_V1_PREFIX + "/statistic/user-out")
                .content(asJsonString(req))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }
}
