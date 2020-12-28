package ee.gaile.controller.proxy;

import ee.gaile.CVApplication;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CVApplication.class)
@AutoConfigureMockMvc
class ProxyControllerTest {

    @Test
    void getGraphData() {
    }
}
