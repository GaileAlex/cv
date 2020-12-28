package ee.gaile;

import ee.gaile.sync.mindly.BitfinexAccessService;
import ee.gaile.sync.proxy.ProxyCheck;
import ee.gaile.sync.proxy.ProxyListService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

public class EnvironmentTest {
    @MockBean
    private BitfinexAccessService bitfinexAccessService;

    @MockBean
    private ProxyListService proxyListService;

    @MockBean
    ProxyCheck proxyCheck;
}
