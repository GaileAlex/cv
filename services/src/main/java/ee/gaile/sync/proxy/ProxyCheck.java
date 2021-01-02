package ee.gaile.sync.proxy;

import ee.gaile.entity.proxy.ProxyList;
import ee.gaile.repository.proxy.ProxyRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class ProxyCheck {
    private static final Logger ERROR_LOG = LoggerFactory.getLogger("error-log");

    private final ProxyRepository proxyRepository;
    private static final String FILE_URL = "http://ipv4.ikoula.testdebit.info/1M.iso";
    private static final Double FILE_SIZE = 1_000_000.0;
    private static final Integer TIMEOUT = 60_000;

    @Async
    public void checkProxy(ProxyList proxyList) {

        Proxy socksProxy = new Proxy(Proxy.Type.SOCKS,
                new InetSocketAddress(proxyList.getIpAddress(), proxyList.getPort()));
        File tempFile = new File(proxyList.getId() + "_" + proxyList.getIpAddress()
                + "_" + proxyList.getPort() + ".tmp");
        try (OutputStream outStream = new FileOutputStream(tempFile)) {
            URL fileUrl = new URL(FILE_URL);
            LocalDateTime start = LocalDateTime.now();

            proxyList.setLastChecked(LocalDateTime.now());

            HttpURLConnection socksConnection = (HttpURLConnection) fileUrl.openConnection(socksProxy);
            socksConnection.setConnectTimeout(TIMEOUT);
            socksConnection.setReadTimeout(TIMEOUT);

            socksConnection.getResponseCode();

            proxyList.setResponse(Duration.between(start.toLocalTime(), LocalDateTime.now().toLocalTime()).toMillis());

            LocalDateTime startFile = LocalDateTime.now();
            InputStream inputStream = socksConnection.getInputStream();

            byte[] buffer = new byte[8 * 1024];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();

            proxyList.setSpeed(checkSpeed(startFile, LocalDateTime.now()));

            Double uptime = getUptime(proxyList);
            proxyList.setUptime(uptime);

            proxyRepository.save(proxyList);

            socksConnection.disconnect();

        } catch (IOException e) {
            if (proxyList.getNumberUnansweredChecks() != null) {
                proxyList.setNumberUnansweredChecks(proxyList.getNumberUnansweredChecks() + 1);
            } else {
                proxyList.setNumberUnansweredChecks(1);
            }
            Double uptime = getUptime(proxyList);
            proxyList.setUptime(uptime);
            proxyList.setSpeed(0.0);
            proxyRepository.save(proxyList);

        }

        try {
            Files.deleteIfExists(Paths.get(proxyList.getId() + "_" +
                    proxyList.getIpAddress() + "_" + proxyList.getPort() + ".tmp"));
        } catch (IOException e) {
            ERROR_LOG.error("failed to delete file: " + proxyList.getId() + "_" +
                    proxyList.getIpAddress() + "_" + proxyList.getPort() + ".tmp");
        }

    }

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

    private Double checkSpeed(LocalDateTime start, LocalDateTime now) {
        long duration;

        if (Duration.between(start.toLocalTime(), now).toMillis() == 0) {
            duration = 20L;
        } else {
            duration = Duration.between(start.toLocalTime(), now).toMillis();
        }

        return FILE_SIZE / duration;

    }
}
