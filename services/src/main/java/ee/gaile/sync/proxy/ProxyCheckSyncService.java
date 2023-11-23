package ee.gaile.sync.proxy;

import ee.gaile.entity.proxy.ProxyEntity;
import ee.gaile.repository.proxy.ProxyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.*;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

/**
 * Service for checking proxy for availability
 *
 * @author Aleksei Gaile
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProxyCheckSyncService {
    private static final String FILE_URL = "https://gaile.ee/assets/1M.iso";
    private static final String GOOGLE_URL = "google.com";
    private static final Double FILE_SIZE = 1_000_000.0;
    private static final Integer TIMEOUT = 60_000;

    private final ProxyRepository proxyRepository;

    /**
     * Checks the proxy list for the ability to connect and download the file
     *
     * @param proxyEntity - proxy
     */
    public void checkProxy(ProxyEntity proxyEntity) {
        if (!checkInternetConnection()) {
            return;
        }

        Proxy socksProxy;
        try {
            socksProxy = new Proxy(Proxy.Type.SOCKS,
                    new InetSocketAddress(proxyEntity.getIpAddress(), proxyEntity.getPort()));
        } catch (IllegalArgumentException e) {
            proxyRepository.delete(proxyEntity);
            return;
        }

        try {
            LocalDateTime startConnection = LocalDateTime.now();

            URLConnection socksConnection = new URL(FILE_URL)
                    .openConnection(socksProxy);
            socksConnection.setConnectTimeout(TIMEOUT);
            socksConnection.setReadTimeout(TIMEOUT);

            proxyEntity.setResponse(Duration.between(startConnection.toLocalTime(), LocalDateTime.now().toLocalTime()).toMillis());

            Instant startFile = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant();
            InputStream inputStream = socksConnection.getInputStream();

            socksConnection.getInputStream();

            inputStream.close();

            proxyEntity.setSpeed(checkSpeed(startFile));
            proxyEntity.setNumberChecks(proxyEntity.getNumberChecks() + 1);
            double uptime = getUptime(proxyEntity);
            proxyEntity.setUptime(uptime);
            proxyEntity.setLastChecked(LocalDateTime.now());
            proxyEntity.setLastSuccessfulCheck(LocalDateTime.now());

            proxyRepository.save(proxyEntity);

        } catch (Exception e) {
            saveUnansweredCheck(proxyEntity);
        }
    }

    private void saveUnansweredCheck(ProxyEntity proxyEntity) {
        double uptime = getUptime(proxyEntity);
        proxyEntity.setUptime(uptime);
        proxyEntity.setSpeed(0.0);
        proxyEntity.setNumberChecks(proxyEntity.getNumberChecks() + 1);

        if (proxyEntity.getNumberUnansweredChecks() != null) {
            proxyEntity.setNumberUnansweredChecks(proxyEntity.getNumberUnansweredChecks() + 1);
        } else {
            proxyEntity.setNumberUnansweredChecks(1);
        }

        proxyEntity.setLastChecked(LocalDateTime.now());

        proxyRepository.save(proxyEntity);
    }

    /**
     * Set uptime
     *
     * @param proxyEntity - proxy
     * @return Double - uptime
     */
    private Double getUptime(ProxyEntity proxyEntity) {
        Integer numberChecks = proxyEntity.getNumberChecks();
        int numberUnansweredChecks;
        if (proxyEntity.getNumberUnansweredChecks() == null) {
            numberUnansweredChecks = 0;
        } else {
            numberUnansweredChecks = proxyEntity.getNumberUnansweredChecks();
        }

        double numberChecksValue = 100.0 * ((double) numberUnansweredChecks / (double) numberChecks);

        if (Double.isInfinite(numberChecksValue) || Double.isNaN(numberChecksValue)) {
            numberChecksValue = 100.0;
        }

        return 100.0 - numberChecksValue;
    }

    /**
     * Sets the download speed of the file
     *
     * @param start -download start time
     * @return Double - file download speed
     */
    private Double checkSpeed(Instant start) {
        Instant now = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant();
        long duration = ChronoUnit.MILLIS.between(start, now);
        double speed = FILE_SIZE / duration;

        if (Double.isInfinite(speed)) {
            return 50000.0;
        }

        return speed;
    }

    private boolean checkInternetConnection() {
        try {
            return InetAddress.getByName(GOOGLE_URL).getHostName().equals(GOOGLE_URL);
        } catch (IOException e) {
            return false;
        }
    }

}
