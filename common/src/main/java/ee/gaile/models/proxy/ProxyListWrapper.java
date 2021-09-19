package ee.gaile.models.proxy;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProxyListWrapper {

    private List<Proxy> proxyLists;

    private Long total;

}
