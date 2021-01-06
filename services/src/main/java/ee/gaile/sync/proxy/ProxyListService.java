package ee.gaile.sync.proxy;

import ee.gaile.entity.proxy.ProxyList;
import ee.gaile.repository.proxy.ProxyRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ProxyListService {
    private static final Logger ERROR_LOG = LoggerFactory.getLogger("error-log");
    private final ProxyRepository proxyRepository;
    private final ProxyCheck proxyCheck;

    @Value("${proxy.scheduled.run}")
    private Boolean isRun;

    public ProxyListService(ProxyRepository proxyRepository,
                            ProxyCheck proxyCheck) {
        this.proxyRepository = proxyRepository;
        this.proxyCheck = proxyCheck;
    }

    @Scheduled(fixedDelay = Long.MAX_VALUE)
    public void firstStartSyncService() {
        setAllProxy();
    }

    @Scheduled(cron = "${proxy.scheduled}")
    public void setAllProxy() {
        if (!isRun) {
            return;
        }

        List<ProxyList> proxyLists = proxyRepository.findAllByUptime();

        log.info("Start proxy list sync. Size lists is {}", proxyLists.size());

        for (ProxyList proxyList : proxyLists) {
            if (!doFirstCheck(proxyList)) {
                continue;
            }
            proxyCheck.checkProxy(proxyList);
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
                && proxyList.getUptime() < 1 && proxyList.getNumberUnansweredChecks() > 1500) {
            try {
                Files.deleteIfExists(Paths.get(proxyList.getId() + "_"
                        + proxyList.getIpAddress() + "_" + proxyList.getPort() + ".tmp"));
            } catch (IOException e) {
                ERROR_LOG.error("failed to delete file: " + proxyList.getIpAddress() + proxyList.getPort() + "tempFile.tmp");
            }

            proxyRepository.delete(proxyList);
            return false;
        }
        return true;
    }

}
