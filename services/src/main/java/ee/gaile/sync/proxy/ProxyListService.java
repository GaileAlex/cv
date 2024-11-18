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
    private static final int THREAD_POOL = 80;
    private static final int ALLOWABLE_PROXY = 100;
    private static final int NUMBER_UNANSWERED_CHECKS = 10;
    private static final int ONE_MONTH = 30;

    private final ProxyRepository proxyRepository;
    private final NewProxyService newProxyService;
    private final ProxyCheckSyncService proxyCheckSyncService;
    private final ProxyMapper proxyMapper;

    private ExecutorService proxyListsExecutor = Executors.newFixedThreadPool(THREAD_POOL,
            new CustomizableThreadFactory("proxy-sync-"));

    /**
     * The start of the proxy check service
     */
    @Override
    public void sync() {
        checkActiveThreadPool();

        long aliveProxies = proxyRepository.getTotalAliveProxies();
        if (aliveProxies < ALLOWABLE_PROXY) {
            newProxyService.setNewProxy();
        }

        List<ProxyEntity> proxyEntities = proxyRepository.findAllOrderByRandom();
        List<Proxy> proxies = proxyMapper.map(proxyEntities);

        log.info("Start proxy list sync. Size lists is {}, in total there were {}, thread pool {}",
                proxyEntities.size(), aliveProxies, ((ThreadPoolExecutor) proxyListsExecutor).getCorePoolSize());

        proxies.forEach(proxy -> proxyListsExecutor.execute(() -> {
            if (doFirstCheck(proxy)) {
                proxyCheckSyncService.checkProxy(proxy);
            }
        }));
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

        if (proxy.getUptime() == 0 && proxy.getNumberUnansweredChecks() > NUMBER_UNANSWERED_CHECKS ||
                Objects.nonNull(proxy.getLastSuccessfulCheck()) &&
                        DAYS.between(proxy.getLastSuccessfulCheck(), LocalDateTime.now()) > ONE_MONTH &&
                        proxy.getUptime() < 5) {

            proxyRepository.delete(proxyRepository.findById(proxy.getId()).get());

            return false;
        }

        return true;
    }

    /**
     * Checks the end of the previous schedule
     */
    private void checkActiveThreadPool() {
        int activeThreadPool = ((ThreadPoolExecutor) proxyListsExecutor).getActiveCount();
        if (activeThreadPool > 0) {
            log.warn("The previous sync is incomplete, canceled. Unfinished tasks - {}", activeThreadPool);
            proxyListsExecutor.shutdownNow();
            proxyListsExecutor = Executors.newFixedThreadPool(THREAD_POOL,
                    new CustomizableThreadFactory("proxy-sync-"));
        }
    }

}
