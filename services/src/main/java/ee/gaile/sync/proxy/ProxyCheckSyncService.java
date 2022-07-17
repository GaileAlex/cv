package ee.gaile.sync.proxy;

import ee.gaile.entity.proxy.ProxyEntity;
import ee.gaile.repository.proxy.ProxyRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Service for checking proxy for availability
 *
 * @author Aleksei Gaile
 */
@Slf4j
@Service
@AllArgsConstructor
public class ProxyCheckSyncService {
    private static final String FILE_URL = "http://ipv4.ikoula.testdebit.info/10M.iso";
    private static final String GOOGLE_URL = "google.com";
    private static final Double FILE_SIZE = 10_000_000.0;
    private static final Integer TIMEOUT = 60_000;

    private final ProxyRepository proxyRepository;

    /**
     * Checks the proxy list for the ability to connect and download the file
     *
     * @param proxyEntity - proxy
     */
    @SuppressWarnings("StatementWithEmptyBody")
    public void checkProxy(ProxyEntity proxyEntity) {
        if (!checkInternetConnection()) {
            return;
        }

        Proxy socksProxy = new Proxy(Proxy.Type.SOCKS,
                new InetSocketAddress(proxyEntity.getIpAddress(), proxyEntity.getPort()));

        try {
            URL fileUrl = new URL(FILE_URL);
            LocalDateTime startConnection = LocalDateTime.now();

            HttpURLConnection socksConnection = (HttpURLConnection) fileUrl.openConnection(socksProxy);
            socksConnection.setConnectTimeout(TIMEOUT);
            socksConnection.setReadTimeout(TIMEOUT);
            socksConnection.getResponseCode();

            proxyEntity.setResponse(Duration.between(startConnection.toLocalTime(), LocalDateTime.now().toLocalTime()).toMillis());

            LocalDateTime startFile = LocalDateTime.now();
            InputStream inputStream = socksConnection.getInputStream();

            byte[] buffer = new byte[8 * 1024];
            while ((inputStream.read(buffer)) != -1) ;

            socksConnection.disconnect();
            inputStream.close();

            proxyEntity.setSpeed(checkSpeed(startFile, LocalDateTime.now(), proxyEntity.getId()));
            proxyEntity.setNumberChecks(proxyEntity.getNumberChecks() + 1);
            double uptime = getUptime(proxyEntity);
            proxyEntity.setUptime(uptime);
            proxyEntity.setLastChecked(LocalDateTime.now());

            proxyRepository.save(proxyEntity);

        } catch (Exception e) {
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
    }

    /**
     * Set uptime
     *
     * @param proxyEntity - proxy
     * @return Double - uptime
     */
    private Double getUptime(ProxyEntity proxyEntity) {
        Integer numberChecks = proxyEntity.getNumberChecks();
        Integer numberUnansweredChecks;
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
     * @param now   - download end time
     * @return Double - file download speed
     */
    private Double checkSpeed(LocalDateTime start, LocalDateTime now, Long id) {
        long duration = Duration.between(start.toLocalTime(), now).toMillis();
        double speed = FILE_SIZE / duration;

        if (Double.isInfinite(speed)) {
            log.info("proxy speed is infinite. ID is {}", id);
            return 0.0;
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
