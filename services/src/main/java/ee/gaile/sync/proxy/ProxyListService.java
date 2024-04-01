package ee.gaile.sync.proxy;

import ee.gaile.entity.proxy.ProxyEntity;
import ee.gaile.repository.proxy.ProxyRepository;
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
    private static final int THREAD_POOL = 150;
    private static final int ALLOWABLE_PROXY = 150;
    private static final int NUMBER_UNANSWERED_CHECKS = 10;
    private static final int ONE_MONTH = 30;

    private final ProxyRepository proxyRepository;
    private final NewProxyService newProxyService;
    private final ProxyCheckSyncService proxyCheckSyncService;

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

        log.info("Start proxy list sync. Size lists is {}, in total there were {}, thread pool {}",
                proxyEntities.size(), aliveProxies, ((ThreadPoolExecutor) proxyListsExecutor).getCorePoolSize());

        proxyEntities.forEach(proxyList -> proxyListsExecutor.execute(() -> {
            if (doFirstCheck(proxyList)) {
                proxyCheckSyncService.checkProxy(proxyList);
            }
        }));
    }

    /**
     * Sets the first check and removes inactive proxies
     *
     * @param proxyEntity - proxy list
     * @return boolean
     */
    private boolean doFirstCheck(ProxyEntity proxyEntity) {
        if (proxyEntity.getFirstChecked() == null) {
            proxyEntity.setFirstChecked(LocalDateTime.now());
            proxyEntity.setAnonymity("High anonymity");
            proxyEntity.setNumberChecks(0);
            proxyEntity.setNumberUnansweredChecks(0);
            proxyEntity.setUptime(0.0);
        }

        if (proxyEntity.getUptime() == 0 && proxyEntity.getNumberUnansweredChecks() > NUMBER_UNANSWERED_CHECKS ||
                Objects.nonNull(proxyEntity.getLastSuccessfulCheck()) &&
                        DAYS.between(proxyEntity.getLastSuccessfulCheck(), LocalDateTime.now()) > ONE_MONTH &&
                        proxyEntity.getUptime() < 5) {
            proxyRepository.delete(proxyEntity);

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
