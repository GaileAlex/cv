package ee.gaile.models;

import ee.gaile.entity.ProxyList;
import lombok.Data;

import java.util.List;

@Data
public class ProxyListWrapper {

    private List<ProxyList> proxyLists;
    public Long total;

}
