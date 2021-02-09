package ee.gaile.sync.proxy;

import ee.gaile.entity.proxy.ProxyList;
import ee.gaile.repository.proxy.ProxyRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Checks proxy list
 *
 * @author Aleksei Gaile
 */
@Slf4j
@Service
@AllArgsConstructor
public class ProxyCheckSyncService {
    private final ProxyRepository proxyRepository;
    private static final String FILE_URL = "http://ipv4.ikoula.testdebit.info/10M.iso";
    private static final Double FILE_SIZE = 10_000_000.0;
    private static final Integer TIMEOUT = 60_000;

    /**
     * Checks the proxy list for the ability to connect and download the file
     *
     * @param proxyList - proxy
     */
    @SuppressWarnings("StatementWithEmptyBody")
    public void checkProxy(ProxyList proxyList) {
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

            proxyList.setSpeed(checkSpeed(startFile, LocalDateTime.now()));

            Double uptime = getUptime(proxyList);
            proxyList.setUptime(uptime);
            proxyList.setLastChecked(LocalDateTime.now());

            proxyRepository.save(proxyList);

        } catch (IOException e) {
            if (proxyList.getNumberUnansweredChecks() != null) {
                proxyList.setNumberUnansweredChecks(proxyList.getNumberUnansweredChecks() + 1);
            } else {
                proxyList.setNumberUnansweredChecks(1);
            }
            Double uptime = getUptime(proxyList);
            proxyList.setUptime(uptime);
            proxyList.setSpeed(0.0);
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
        return 100.0 - 100.0 * ((double) numberUnansweredChecks / (double) numberChecks);
    }

    /**
     * Sets the download speed of the file
     *
     * @param start -download start time
     * @param now   - download end time
     * @return Double - file download speed
     */
    private Double checkSpeed(LocalDateTime start, LocalDateTime now) {
        long duration = Duration.between(start.toLocalTime(), now).toMillis();

        return FILE_SIZE / duration;
    }
}
