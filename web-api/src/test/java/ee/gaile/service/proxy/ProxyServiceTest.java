package ee.gaile.service.proxy;

import ee.gaile.ApplicationIT;
import ee.gaile.models.proxy.ProxyListWrapper;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ProxyServiceTest extends ApplicationIT {

    @Autowired
    private ProxyService proxyService;

    @Test
    void getProxy() {
        ProxyListWrapper proxyListWrapper = proxyService.getProxy(10, 0);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(proxyListWrapper.getTotal()).isEqualTo(1);
            softly.assertThat(proxyListWrapper.getProxyLists().get(0).getIpAddress()).isEqualTo("208.113.153.233");
            softly.assertThat(proxyListWrapper.getProxyLists().get(0).getProtocol()).isEqualTo("SOCKS5");
            softly.assertThat(proxyListWrapper.getProxyLists().get(0).getSpeed()).isEqualTo(10.0);
            softly.assertThat(proxyListWrapper.getProxyLists().get(0).getUptime()).isEqualTo(40.0);
        });
    }
}
