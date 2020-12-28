package ee.gaile.service.proxy;

import ee.gaile.sync.proxy.ProxyListService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

class ProxyServiceTest {

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
