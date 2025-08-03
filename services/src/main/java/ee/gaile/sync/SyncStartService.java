package ee.gaile.sync;

import ee.gaile.sync.proxy.CountrySyncService;
import ee.gaile.sync.proxy.ProxyListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Synchronization start service
 *
 * @author Aleksei Gaile
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SyncStartService {
    private final ProxyListService proxyListService;
    private final CountrySyncService countrySyncService;
    private final SyncService englishServiceSync;

    @Value("${english.list.scheduled.run}")
    private boolean isRunEnglish;
    @Value("${proxy.list.scheduled.run}")
    private boolean isRunProxy;
    @Value("${proxy.country.scheduled.run}")
    private boolean isRunCheckCountry;

    /**
     * Starts syncing proxy list
     */
    @Scheduled(cron = "${proxy.list.scheduled}")
    public void syncProxy() {
        if (!isRunProxy) {
            log.info("Scheduled proxy list sync disable");
            return;
        }
        proxyListService.sync();
    }

    @Scheduled(cron = "${english.list.scheduled}")
    public void syncEnglish() {
        if (!isRunEnglish) {
            log.info("English list sync disable");
            return;
        }
        englishServiceSync.sync();
    }

    public void manualSyncProxy() {
        proxyListService.sync();
    }

    /**
     * Starts synchronization of unknown proxy countries
     */
    @Scheduled(cron = "${proxy.country.scheduled}")
    public void syncCheckCountry() {
        if (!isRunCheckCountry) {
            log.info("Scheduled proxy country sync disable");
            return;
        }
        countrySyncService.sync();
    }

}
