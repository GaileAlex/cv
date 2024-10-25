package ee.gaile.sync.proxy;

import ee.gaile.entity.proxy.ProxyEntity;
import ee.gaile.repository.proxy.ProxyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
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
    private static final String FILE_URL = "https://gaile.ee/api/v1/proxy/file";
    private static final String GOOGLE_URL = "google.com";
    private static final Double FILE_SIZE = 10_000_000.0;
    private static final Integer TIMEOUT = 3;

    private final ProxyRepository proxyRepository;
    private final RestTemplate restTemplate;

    /**
     * Checks the proxy and updates the database accordingly.
     * If internet connection is not available, the proxy is saved to the database.
     * If the proxy is invalid, it is deleted from the database.
     * If the check fails, the unanswered check is saved to the database.
     *
     * @param proxyEntity The proxy entity to be checked
     */
    public void checkProxy(ProxyEntity proxyEntity) {
        if (!checkInternetConnection()) {
            proxyRepository.save(proxyEntity);
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
            Instant startFile = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant();

            requestToUrl(socksProxy);

            proxyEntity.setSpeed(checkSpeed(startFile));
            proxyEntity.setNumberChecks(proxyEntity.getNumberChecks() + 1);
            double uptime = getUptime(proxyEntity);
            proxyEntity.setUptime(uptime);
            proxyEntity.setLastChecked(LocalDateTime.now());
            proxyEntity.setLastSuccessfulCheck(LocalDateTime.now());

            proxyRepository.save(proxyEntity);
        } catch (RestClientException e) {
            saveUnansweredCheck(proxyEntity);
        }
    }

    /**
     * Updates the proxyEntity with the latest uptime, speed, and check counts, and saves it to the repository.
     * Increments the number of unanswered checks if applicable.
     *
     * @param proxyEntity the ProxyEntity to be updated and saved
     */
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
     * Calculate the uptime percentage based on the number of checks and unanswered checks.
     *
     * @param proxyEntity the ProxyEntity containing the number of checks and unanswered checks
     * @return the uptime percentage
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
     * Calculates the speed of a process based on the duration since the start time.
     *
     * @param start the start time of the process
     * @return the speed of the process
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

    /**
     * Check internet connection by trying to resolve Google's URL.
     *
     * @return true if internet connection is available, false otherwise.
     */
    private boolean checkInternetConnection() {
        try (Socket socket = new Socket()) {
            InetSocketAddress socketAddress = new InetSocketAddress(GOOGLE_URL, 80);
            socket.connect(socketAddress, 1000);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Makes a GET request to a file URL with specified headers using a RestTemplate set up with a specified SOCKS proxy.
     *
     * @param socksProxy the SOCKS proxy to be used
     */
    private void requestToUrl(Proxy socksProxy) {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setProxy(socksProxy);
        requestFactory.setConnectTimeout(Duration.ofMinutes(TIMEOUT));
        requestFactory.setReadTimeout(Duration.ofMinutes(TIMEOUT));
        restTemplate.setRequestFactory(requestFactory);

        restTemplate.exchange(FILE_URL, HttpMethod.GET, null, byte[].class);
    }

}
