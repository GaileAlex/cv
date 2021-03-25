package ee.gaile.sync.proxy;

import ee.gaile.entity.proxy.ProxyList;
import ee.gaile.repository.proxy.ProxyRepository;
import ee.gaile.sync.SyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Proxy list sync service
 *
 * @author Aleksei Gaile
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProxyListService implements SyncService {
    private static final int THREAD_POOL = 150;
    private static final int ALLOWABLE_PROXY = 120;
    private static final int NUMBER_UNANSWERED_CHECKS = 200;

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

        setCorePoolSize();

        List<ProxyList> proxyLists = proxyRepository.findAllOrderByRandom();

        log.info("Start proxy list sync. Size lists is {}, in total there were {}, thread pool {}",
                proxyLists.size(), aliveProxies, ((ThreadPoolExecutor) proxyListsExecutor).getCorePoolSize());

        proxyLists.forEach(proxyList -> proxyListsExecutor.execute(() -> {
            if (doFirstCheck(proxyList)) {
                proxyCheckSyncService.checkProxy(proxyList);
            }
        }));
    }

    /**
     * Sets the first check and removes inactive proxies
     *
     * @param proxyList - proxy list
     * @return boolean
     */
    private boolean doFirstCheck(ProxyList proxyList) {
        if (proxyList.getNumberChecks() == null) {
            proxyList.setNumberChecks(0);
        }

        if (proxyList.getFirstChecked() == null) {
            proxyList.setFirstChecked(LocalDateTime.now());
            proxyList.setAnonymity("High anonymity");
            proxyRepository.save(proxyList);
        }

        if (proxyList.getUptime() != null &&
                proxyList.getNumberUnansweredChecks() != null &&
                proxyList.getUptime() < 5 &&
                proxyList.getNumberUnansweredChecks() > NUMBER_UNANSWERED_CHECKS) {

            proxyRepository.delete(proxyList);

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

    /**
     * Sets core pool size
     */
    private void setCorePoolSize() {
        int threadPool = proxyRepository.getTotal() / THREAD_POOL;

        ((ThreadPoolExecutor) proxyListsExecutor).setCorePoolSize(threadPool);
        ((ThreadPoolExecutor) proxyListsExecutor).setMaximumPoolSize(threadPool);
    }

}
