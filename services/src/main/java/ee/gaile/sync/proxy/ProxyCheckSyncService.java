package ee.gaile.sync.proxy;

import ee.gaile.entity.proxy.ProxyList;
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
    private static final String GOOGLE_URL = "http://www.google.com";
    private static final Double FILE_SIZE = 10_000_000.0;
    private static final Integer TIMEOUT = 60_000;

    private final ProxyRepository proxyRepository;

    /**
     * Checks the proxy list for the ability to connect and download the file
     *
     * @param proxyList - proxy
     */
    @SuppressWarnings("StatementWithEmptyBody")
    public void checkProxy(ProxyList proxyList) {
        if (!checkInternetConnection()) {
            return;
        }

        Proxy socksProxy = new Proxy(Proxy.Type.SOCKS,
                new InetSocketAddress(proxyList.getIpAddress(), proxyList.getPort()));

        try {
            URL fileUrl = new URL(FILE_URL);
            LocalDateTime startConnection = LocalDateTime.now();

            HttpURLConnection socksConnection = (HttpURLConnection) fileUrl.openConnection(socksProxy);
            socksConnection.setConnectTimeout(TIMEOUT);
            socksConnection.setReadTimeout(TIMEOUT);
            socksConnection.getResponseCode();

            proxyList.setResponse(Duration.between(startConnection.toLocalTime(), LocalDateTime.now().toLocalTime()).toMillis());

            LocalDateTime startFile = LocalDateTime.now();
            InputStream inputStream = socksConnection.getInputStream();

            byte[] buffer = new byte[8 * 1024];
            while ((inputStream.read(buffer)) != -1) ;

            socksConnection.disconnect();
            inputStream.close();

            proxyList.setSpeed(checkSpeed(startFile, LocalDateTime.now(), proxyList.getId()));
            proxyList.setNumberChecks(proxyList.getNumberChecks() + 1);
            double uptime = getUptime(proxyList);
            proxyList.setUptime(uptime);
            proxyList.setLastChecked(LocalDateTime.now());

            proxyRepository.save(proxyList);

        } catch (IOException e) {
            double uptime = getUptime(proxyList);
            proxyList.setUptime(uptime);
            proxyList.setSpeed(0.0);
            proxyList.setNumberChecks(proxyList.getNumberChecks() + 1);

            if (proxyList.getNumberUnansweredChecks() != null) {
                proxyList.setNumberUnansweredChecks(proxyList.getNumberUnansweredChecks() + 1);
            } else {
                proxyList.setNumberUnansweredChecks(1);
            }

            proxyList.setLastChecked(LocalDateTime.now());

            proxyRepository.save(proxyList);
        }
    }

    /**
     * Set uptime
     *
     * @param proxyList - proxy
     * @return Double - uptime
     */
    private Double getUptime(ProxyList proxyList) {
        Integer numberChecks = proxyList.getNumberChecks();
        Integer numberUnansweredChecks;
        if (proxyList.getNumberUnansweredChecks() == null) {
            numberUnansweredChecks = 0;
        } else {
            numberUnansweredChecks = proxyList.getNumberUnansweredChecks();
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
            URL url = new URL(GOOGLE_URL);
            URLConnection connection = url.openConnection();
            connection.connect();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

}
