package ee.gaile.sync.proxy;

import ee.gaile.entity.proxy.ProxyList;
import ee.gaile.enums.ProxySites;
import ee.gaile.repository.proxy.ProxyRepository;
import ee.gaile.sync.SyncService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Runs synchronization services on a schedule
 *
 * @author Aleksei Gaile
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProxyListService implements SyncService {
    private static final int THREAD_POOL = 200;
    private static final int ALLOWABLE_AMOUNT = 120;

    private final ProxyRepository proxyRepository;
    private final ProxyCheckSyncService proxyCheckSyncService;

    @Getter
    ExecutorService proxyListsExecutor = Executors.newFixedThreadPool(THREAD_POOL);

    /**
     * Checks the end of the previous schedule and the start of the proxy check service
     */
    @Override
    public void sync() {
        if (((ThreadPoolExecutor) proxyListsExecutor).getActiveCount() > 0) {
            log.warn("Proxy list sync canceled. The previous sync is incomplete.");
            return;
        }

        Long aliveProxies = proxyRepository.getTotal();

        if (aliveProxies < ALLOWABLE_AMOUNT) {
            setNewProxy();
        }

        List<ProxyList> proxyLists = proxyRepository.findAllOrderByRandom();

        log.info("Start proxy list sync. Size lists is {}, in total there were {}",
                proxyLists.size(), aliveProxies);

        proxyLists.forEach(proxyList -> proxyListsExecutor.execute(() -> {
            if (doFirstCheck(proxyList)) {
                proxyCheckSyncService.checkProxy(proxyList);
            }
        }));
    }

    /**
     * Sets the number of checks and removes inactive proxies
     *
     * @param proxyList - proxy list
     * @return boolean
     */
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

    /**
     * Searches for new proxies on sites, adds to the database
     */
    public void setNewProxy() {
        List<ProxyList> proxyLists = new ArrayList<>();

        for (ProxySites proxySites : ProxySites.values()) {
            try {
                Document doc = Jsoup.connect(proxySites.getUrl()).timeout(0).get();

                Elements table = doc.select("table");
                Elements rows = table.select("tr");

                for (int i = 1; i < rows.size(); i++) {
                    Element row = rows.get(i);

                    String[] parts = row.toString().split("[^0-9.0-9]");
                    ProxyList proxyList = new ProxyList();

                    for (String ip : parts) {

                        if (ip.matches("^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.)" +
                                "{3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$")) {
                            proxyList.setIpAddress(ip);
                            continue;
                        }
                        if (proxyList.getIpAddress() != null && !ip.equals("")) {
                            proxyList.setPort(Integer.valueOf(ip));
                            proxyList.setProtocol("SOCKS5");
                            proxyList.setCountry("unknown");
                            proxyLists.add(proxyList);
                            break;
                        }
                    }
                }
            } catch (IOException e) {
                log.error("Site connection error {} ", proxySites);
            }
        }

        int counter = 0;
        for (ProxyList proxyList : proxyLists) {
            try {
                proxyRepository.save(proxyList);
                counter++;
            } catch (Exception ignored) {
            }
        }

        log.warn("Proxy saved - {}", counter);
    }
}
