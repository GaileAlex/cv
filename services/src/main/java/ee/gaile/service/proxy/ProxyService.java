package ee.gaile.service.proxy;

import ee.gaile.entity.proxy.ProxyList;
import ee.gaile.models.proxy.ProxyListWrapper;
import ee.gaile.repository.proxy.ProxyRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Getting data for api
 *
 * @author Aleksei Gaile
 */
@Slf4j
@Service
@AllArgsConstructor
public class ProxyService {
    private final ProxyRepository proxyRepository;

    /**
     * getting data for proxy table
     *
     * @param pageSize - number of pages in the table
     * @param page     - page number
     * @return - ProxyListWrapper proxy list and number of pages
     */
    public ProxyListWrapper getProxy(Integer pageSize, Integer page) {
        List<ProxyList> proxyLists = proxyRepository.findWithPaging(pageSize, page * pageSize);
        ProxyListWrapper responseWrapper = new ProxyListWrapper();
        responseWrapper.setProxyLists(proxyLists);
        responseWrapper.setTotal(proxyRepository.getTotalAliveProxiesLimit());

        return responseWrapper;
    }

}
