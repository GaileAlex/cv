package ee.gaile.sync.proxy;

import ee.gaile.entity.proxy.ProxyEntity;
import ee.gaile.models.proxy.Proxy;
import ee.gaile.repository.proxy.ProxyRepository;
import ee.gaile.service.mapper.ProxyMapper;
import ee.gaile.sync.SyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * @author Aleksei Gaile
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProxyListService implements SyncService {
    private static final int ALLOWABLE_PROXY = 100;
    private static final int NUMBER_UNANSWERED_CHECKS = 10;
    private static final int ONE_MONTH = 30;

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
        List<Proxy> proxiesForCheck = new ArrayList<>();

        log.info("Start proxy list sync. Size lists is {}, in total there were {}",
                proxyEntities.size(), aliveProxies);

        proxies.forEach(proxy -> {
            if (doFirstCheck(proxy)) {
                proxiesForCheck.add(proxy);
            }
        });

        proxyCheckSyncService.checkAllAsync(proxiesForCheck);
    }

    /**
     * Sets the first check and removes inactive proxies
     *
     * @param proxy - proxy
     * @return boolean
     */
    private boolean doFirstCheck(Proxy proxy) {
        if (proxy.getFirstChecked() == null) {
            proxy.setFirstChecked(LocalDateTime.now());
            proxy.setAnonymity("High anonymity");
            proxy.setNumberChecks(0);
            proxy.setNumberUnansweredChecks(0);
            proxy.setUptime(0.0);
        }

        if (proxy.getPort() > 65535 || proxy.getUptime() == 0 && proxy.getNumberUnansweredChecks() > NUMBER_UNANSWERED_CHECKS ||
                Objects.nonNull(proxy.getLastSuccessfulCheck()) &&
                        DAYS.between(proxy.getLastSuccessfulCheck(), LocalDateTime.now()) > ONE_MONTH &&
                        proxy.getUptime() < 5) {

            proxyRepository.deleteById(proxy.getId());

            return false;
        }

        return true;
    }

}
