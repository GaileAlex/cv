package ee.gaile.sync.proxy;

import ee.gaile.models.proxy.Proxy;
import ee.gaile.repository.proxy.ProxyRepository;
import ee.gaile.service.mapper.ProxyMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.netty.transport.ProxyProvider;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static java.time.temporal.ChronoUnit.DAYS;

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
    private static final Double FILE_SIZE = 1_000_000.0;
    private static final Integer TIMEOUT = 10;
    private static final int THREAD_POOL = 500;
    private static final int NUMBER_UNANSWERED_CHECKS = 10;
    private static final int ONE_MONTH = 30;
    private final Object lock = new Object();
    private Disposable currentSubscription;
    private Queue<Proxy> currentQueue;

    private final ProxyRepository proxyRepository;
    private final ProxyMapper proxyMapper;

    public Mono<Void> checkProxyAsync(Proxy proxy) {
        Instant start = Instant.now();

        HttpClient httpClient = HttpClient
                .create(ConnectionProvider.newConnection())
                .proxy(spec -> spec.type(ProxyProvider.Proxy.SOCKS5)
                        .host(proxy.getIpAddress())
                        .port(proxy.getPort()))
                .responseTimeout(Duration.ofSeconds(TIMEOUT));

        return httpClient
                .get()
                .uri(FILE_URL)
                .responseSingle((resp, body) -> {
                    if (resp.status().code() == HttpStatus.OK.value()) {
                        return body.asByteArray();
                    } else {
                        return Mono.error(new RuntimeException("HTTP error " + resp.status()));
                    }
                })
                .flatMap(bytes -> Mono.fromRunnable(() -> {
                    double speed = checkSpeed(start);
                    proxy.setSpeed(speed);
                    proxy.setNumberChecks(proxy.getNumberChecks() + 1);
                    proxy.setUptime(getUptime(proxy));
                    proxy.setLastChecked(LocalDateTime.now());
                    proxy.setLastSuccessfulCheck(LocalDateTime.now());
                    proxyRepository.save(proxyMapper.mapToProxyEntity(proxy));
                    //    log.info("✅ OK {}:{} → {} bytes, speed={}", proxy.getIpAddress(), proxy.getPort(), bytes.length, speed);
                }).subscribeOn(Schedulers.boundedElastic()))
                .onErrorResume(e -> Mono.fromRunnable(() -> {
                    saveUnansweredCheck(proxy);
                }).subscribeOn(Schedulers.boundedElastic()))
                .then();
    }

    /**
     * Updates the proxyEntity with the latest uptime, speed, and check counts, and saves it to the repository.
     * Increments the number of unanswered checks if applicable.
     *
     * @param proxy the ProxyEntity to be updated and saved
     */
    private void saveUnansweredCheck(Proxy proxy) {
        double uptime = getUptime(proxy);
        proxy.setUptime(uptime);
        proxy.setSpeed(0.0);
        proxy.setNumberChecks(proxy.getNumberChecks() + 1);

        if (proxy.getNumberUnansweredChecks() != null) {
            proxy.setNumberUnansweredChecks(proxy.getNumberUnansweredChecks() + 1);
        } else {
            proxy.setNumberUnansweredChecks(1);
        }

        proxy.setLastChecked(LocalDateTime.now());

        try {
            proxyRepository.save(proxyMapper.mapToProxyEntity(proxy));
        } catch (CannotCreateTransactionException ignored) {
            // ignore
        }

    }

    /**
     * Calculate the uptime percentage based on the number of checks and unanswered checks.
     *
     * @param proxy the Proxy containing the number of checks and unanswered checks
     * @return the uptime percentage
     */
    private Double getUptime(ee.gaile.models.proxy.Proxy proxy) {
        Integer numberChecks = proxy.getNumberChecks();
        int numberUnansweredChecks;
        if (proxy.getNumberUnansweredChecks() == null) {
            numberUnansweredChecks = 0;
        } else {
            numberUnansweredChecks = proxy.getNumberUnansweredChecks();
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
        double speed = FILE_SIZE / duration / 0.8;

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
     * Проверяет список прокси асинхронно с параллельностью 200.
     * Если предыдущая проверка ещё идёт — она прерывается.
     */
    public void checkAllAsync(List<Proxy> proxies) {
        if (!checkInternetConnection()) {
            proxyRepository.saveAll(proxyMapper.mapToProxyEntities(proxies));
            return;
        }

        synchronized (lock) {
            if (currentSubscription != null && !currentSubscription.isDisposed()) {
                currentSubscription.dispose();
                log.info("Previous launch interrupted, remaining proxies: {}", currentQueue.size());
            }

            List<Proxy> proxiesForCheck = new ArrayList<>();
            proxies.forEach(proxy -> {
                if (doFirstCheck(proxy)) {
                    proxiesForCheck.add(proxy);
                }
            });

            currentQueue = new ConcurrentLinkedQueue<>(proxiesForCheck);

            currentSubscription = Flux.fromIterable(currentQueue)
                    .flatMap(proxy ->
                            checkProxyAsync(proxy)
                                    .doFinally(signal -> currentQueue.remove(proxy)), THREAD_POOL)
                    .subscribe(
                            proxy -> {
                            },
                            err -> log.error("Error checking proxy", err),
                            () -> log.info("Checking all proxies completed")
                    );
        }
    }


    /**
     * Sets the first check and removes inactive proxies
     *
     * @param proxy - proxy
     * @return boolean
     */
    private boolean doFirstCheck(Proxy proxy) {
        if (proxy.getFirstChecked() == null) {
            proxy.setFirstChecked(LocalDateTime.now());
            proxy.setAnonymity("High anonymity");
            proxy.setNumberChecks(0);
            proxy.setNumberUnansweredChecks(0);
            proxy.setUptime(0.0);
        }

        if (proxy.getPort() > 65535 || proxy.getUptime() == 0 && proxy.getNumberUnansweredChecks() > NUMBER_UNANSWERED_CHECKS ||
                Objects.nonNull(proxy.getLastSuccessfulCheck()) &&
                        DAYS.between(proxy.getLastSuccessfulCheck(), LocalDateTime.now()) > ONE_MONTH &&
                        proxy.getUptime() < 5) {

            proxyRepository.deleteById(proxy.getId());

            return false;
        }

        return true;
    }

}
