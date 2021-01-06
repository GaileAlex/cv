package ee.gaile.sync;

import ee.gaile.sync.mindly.BitfinexAccessSyncService;
import ee.gaile.sync.proxy.CountrySyncService;
import ee.gaile.sync.proxy.ProxyListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class SyncStartService {
    private final BitfinexAccessSyncService bitfinexAccessSyncService;
    private final ProxyListService proxyListService;
    private final CountrySyncService countrySyncService;

    @Value("${bitfinex.access.run}")
    private boolean isRunBitfinex;
    @Value("${proxy.list.scheduled.run}")
    private boolean isRunProxy;
    @Value("${proxy.country.scheduled.run}")
    private boolean isRunCheckCountry;

    @Scheduled(cron = "${bitfinex.access.scheduled}")
    public void syncBitfinex() {
        if (!isRunBitfinex) {
            log.info("Scheduled proxy list sync disable");
            return;
        }
        bitfinexAccessSyncService.sync();
    }

    @Scheduled(cron = "${proxy.list.scheduled}")
    public void syncProxy() {
        if (!isRunProxy) {
            log.info("Scheduled proxy list sync disable");
            return;
        }
        proxyListService.sync();
    }

    @Scheduled(cron = "${proxy.country.scheduled}")
    public void syncCheckCountry() {
        if (!isRunCheckCountry) {
            log.info("Scheduled proxy list sync disable");
            return;
        }
        countrySyncService.sync();
    }
}
