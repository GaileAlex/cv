package ee.gaile.controller.mindly;

import ee.gaile.CVApplication;
import ee.gaile.EnvironmentTest;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(        classes = CVApplication.class)
@AutoConfigureMockMvc
class MindlyControllerTest  extends EnvironmentTest {

    @Test
    void getPortfolio() {
    }

    @Test
    void addPortfolioItem() {
    }

    @Test
    void deletePortfolioItem() {
    }
}
