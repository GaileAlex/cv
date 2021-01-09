package ee.gaile.sync.proxy;

import ee.gaile.entity.proxy.ProxyList;
import ee.gaile.repository.proxy.ProxyRepository;
import ee.gaile.sync.SyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProxyListService implements SyncService {
    private final ProxyRepository proxyRepository;
    private final ProxyCheckSyncService proxyCheckSyncService;

    @Override
    public void sync() {
        List<ProxyList> proxyLists = proxyRepository.findAllOrderByRandom();

        log.info("Start proxy list sync. Size lists is {}, in total there were {} ",
                proxyLists.size(), proxyRepository.getTotal());

        for (ProxyList proxyList : proxyLists) {
            if (doFirstCheck(proxyList)) {
                proxyCheckSyncService.checkProxy(proxyList);
            }
        }
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
                && proxyList.getUptime() < 5 && proxyList.getNumberUnansweredChecks() > 1000) {
            proxyRepository.delete(proxyList);
            return false;
        }
        return true;
    }

}
