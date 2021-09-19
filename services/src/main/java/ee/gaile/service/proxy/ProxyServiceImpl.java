package ee.gaile.service.proxy;

import ee.gaile.entity.proxy.ProxyEntity;
import ee.gaile.models.proxy.Proxy;
import ee.gaile.models.proxy.ProxyListWrapper;
import ee.gaile.repository.proxy.ProxyRepository;
import ee.gaile.service.utils.MapperUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service data for proxy table
 *
 * @author Aleksei Gaile
 */
@Slf4j
@Service
@AllArgsConstructor
public class ProxyServiceImpl implements ProxyService {
    private final ProxyRepository proxyRepository;
    private final MapperUtils mapperUtils;

    public ProxyListWrapper getProxy(Integer pageSize, Integer page) {
        List<ProxyEntity> proxyEntities = proxyRepository.findWithPaging(pageSize, page * pageSize);
        List<Proxy> proxyList = mapperUtils.toOtherList(proxyEntities, Proxy.class);
        ProxyListWrapper responseWrapper = new ProxyListWrapper();
        responseWrapper.setProxyLists(proxyList);
        responseWrapper.setTotal(proxyRepository.getTotalAliveProxiesLimit());

        return responseWrapper;
    }

}
