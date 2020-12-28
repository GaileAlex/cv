package ee.gaile.models.proxy;

import ee.gaile.entity.proxy.ProxyList;
import lombok.Data;

import java.util.List;

@Data
public class ProxyListWrapper {

    private List<ProxyList> proxyLists;
    public Long total;

}
