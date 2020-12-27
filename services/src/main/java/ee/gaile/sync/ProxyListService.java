package ee.gaile.sync;

import ee.gaile.entity.ProxyList;
import ee.gaile.repository.ProxyRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class ProxyListService {
    private final ProxyRepository proxyRepository;
    private static final String FILE_URL = "http://ipv4.ikoula.testdebit.info/1M.iso";
    private static final Double FILE_SIZE = 1_000_000.0;
    private static final Integer TIMEOUT = 60_000;

    @Scheduled(fixedDelay = Long.MAX_VALUE)
    public void firstStartSyncService() {
        setAllProxy();
    }

    @Scheduled(cron = "${proxy.scheduled}")
    public void setAllProxy() {
        List<ProxyList> proxyLists = proxyRepository.findAllBySpeed();

        for (ProxyList proxyList : proxyLists) {

            if (!doFirstCheck(proxyList)) {
                continue;
            }
            proxyList.setLastChecked(LocalDateTime.now());

            Proxy socksProxy = new Proxy(Proxy.Type.SOCKS,
                    new InetSocketAddress(proxyList.getIpAddress(), proxyList.getPort()));

            try {
                URL fileUrl = new URL(FILE_URL);
                LocalDateTime start = LocalDateTime.now();

                HttpURLConnection socksConnection = (HttpURLConnection) fileUrl.openConnection(socksProxy);
                socksConnection.setConnectTimeout(TIMEOUT);
                socksConnection.setReadTimeout(TIMEOUT);

                socksConnection.getResponseCode();

                proxyList.setResponse(Duration.between(start.toLocalTime(), LocalDateTime.now().toLocalTime()).toMillis());

                LocalDateTime startFile = LocalDateTime.now();
                InputStream inputStream = socksConnection.getInputStream();
                File tempFile = new File("tempFile.tmp");
                OutputStream outStream = new FileOutputStream(tempFile);

                byte[] buffer = new byte[8 * 1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, bytesRead);
                }

                inputStream.close();
                outStream.close();

                proxyList.setSpeed(FILE_SIZE / Duration.between(startFile.toLocalTime(),
                        LocalDateTime.now().toLocalTime()).toMillis());

                Double uptime = getUptime(proxyList);
                proxyList.setUptime(uptime);
                proxyRepository.save(proxyList);

                socksConnection.disconnect();
                tempFile.delete();
                log.info("successful check IP: " + proxyList.getIpAddress());

            } catch (IOException e) {
                log.error("check failed IP: " + proxyList.getIpAddress());
                if (proxyList.getNumberUnansweredChecks() != null) {
                    proxyList.setNumberUnansweredChecks(proxyList.getNumberUnansweredChecks() + 1);
                } else {
                    proxyList.setNumberUnansweredChecks(1);
                }
                Double uptime = getUptime(proxyList);
                proxyList.setUptime(uptime);
                proxyRepository.save(proxyList);
            }
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

        if (proxyList.getUptime() < 20 && proxyList.getNumberUnansweredChecks() > 200) {
            proxyRepository.delete(proxyList);
            return false;
        }
        return true;
    }

}
