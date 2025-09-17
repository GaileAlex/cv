package ee.gaile.sync.proxy;

import ee.gaile.entity.proxy.ProxyEntity;
import ee.gaile.models.proxy.Proxy;
import ee.gaile.repository.proxy.ProxyRepository;
import ee.gaile.service.mapper.ProxyMapper;
import ee.gaile.sync.SyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Aleksei Gaile
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProxyListService implements SyncService {
    private static final int ALLOWABLE_PROXY = 100;

    private final ProxyRepository proxyRepository;
    private final NewProxyService newProxyService;
    private final ProxyCheckSyncService proxyCheckSyncService;
    private final ProxyMapper proxyMapper;

    /**
     * The start of the proxy check service
     */
    @Override
    public void sync() {
        long aliveProxies = proxyRepository.getTotalAliveProxies();
        if (aliveProxies < ALLOWABLE_PROXY) {
            newProxyService.setNewProxy();
        }

        List<ProxyEntity> proxyEntities = proxyRepository.findAllOrderByRandom();
        List<Proxy> proxies = proxyMapper.mapToProxies(proxyEntities);

        proxyCheckSyncService.checkAllAsync(proxies, aliveProxies);
    }

}
