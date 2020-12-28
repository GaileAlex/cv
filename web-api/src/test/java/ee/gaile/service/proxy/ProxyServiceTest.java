package ee.gaile.service.proxy;

import ee.gaile.EnvironmentTest;
import ee.gaile.sync.proxy.ProxyListService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

class ProxyServiceTest   extends EnvironmentTest {

    @MockBean
    private ProxyListService proxyListService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getProxy() {
    }
}
