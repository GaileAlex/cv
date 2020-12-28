package ee.gaile.service.proxy;

import ee.gaile.entity.proxy.ProxyList;
import ee.gaile.models.proxy.ProxyListWrapper;
import ee.gaile.repository.proxy.ProxyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProxyService {
    private final ProxyRepository proxyRepository;

    public ProxyListWrapper getProxy(Integer pageSize, Integer page) {
        List<ProxyList> proxyLists = proxyRepository.findWithPaging(pageSize, page * pageSize);
        ProxyListWrapper responseWrapper = new ProxyListWrapper();
        responseWrapper.setProxyLists(proxyLists);
        responseWrapper.setTotal(proxyRepository.getTotal());

        return responseWrapper;
    }

}
