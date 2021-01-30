package ee.gaile.sync.proxy;

import ee.gaile.entity.proxy.ProxyList;
import ee.gaile.repository.proxy.ProxyRepository;
import ee.gaile.sync.SyncService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProxyListService implements SyncService {
    private static final Integer THREAD_POOL = 80;

    private final ProxyRepository proxyRepository;
    private final ProxyCheckSyncService proxyCheckSyncService;

    @Getter
    ExecutorService proxyListsExecutor = Executors.newFixedThreadPool(THREAD_POOL);

    @Override
    public void sync() {
        if (((ThreadPoolExecutor) proxyListsExecutor).getActiveCount() > 0) {
            log.warn("Proxy list sync canceled. The previous sync is incomplete.");
            return;
        }

        List<ProxyList> proxyLists = proxyRepository.findAllOrderByRandom();

        log.info("Start proxy list sync. Size lists is {}, in total there were {}",
                proxyLists.size(), proxyRepository.getTotal());

        proxyLists.forEach(proxyList -> proxyListsExecutor.execute(() -> {
            if (doFirstCheck(proxyList)) {
                proxyCheckSyncService.checkProxy(proxyList);
            }
        }));
    }

    private boolean doFirstCheck(ProxyList proxyList) {
        if (proxyList.getNumberChecks() != null) {
            proxyList.setNumberChecks(proxyList.getNumberChecks() + 1);
        } else {
            proxyList.setNumberChecks(1);
        }

        if (proxyList.getFirstChecked() == null) {
            proxyList.setFirstChecked(LocalDateTime.now());
            proxyList.setAnonymity("High anonymity");
            proxyRepository.save(proxyList);
        }

        if (proxyList.getUptime() != null && proxyList.getNumberUnansweredChecks() != null
                && proxyList.getUptime() < 5 && proxyList.getNumberUnansweredChecks() > 500) {
            proxyRepository.delete(proxyList);

            log.warn("Proxy deleted. ID {}, uptime {} ", proxyList.getId(), proxyList.getUptime());
            return false;
        }
        return true;
    }

}
