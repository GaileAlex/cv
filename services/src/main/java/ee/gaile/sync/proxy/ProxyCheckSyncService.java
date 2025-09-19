package ee.gaile.sync.proxy;

import ee.gaile.entity.proxy.ProxyEntity;
import ee.gaile.models.proxy.Proxy;
import ee.gaile.repository.proxy.ProxyRepository;
import ee.gaile.service.mapper.ProxyMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.CannotCreateTransactionException;
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
import java.util.concurrent.atomic.AtomicBoolean;

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
    private static final Double FILE_SIZE = 10_000.0;
    private static final Integer TIMEOUT = 10;
    private static final int THREAD_POOL = 250;
    private static final int NUMBER_UNANSWERED_CHECKS = 10;
    private static final int ONE_MONTH = 30;
    private static final int ALLOWABLE_PROXY = 100;
    private Queue<Proxy> currentQueue;
    private final AtomicBoolean starting = new AtomicBoolean(false);

    private final NewProxyService newProxyService;
    private final ProxyRepository proxyRepository;
    private final ProxyMapper proxyMapper;
    private final ConnectionProvider connectionProvider;

    /**
     * Проверяет список прокси асинхронно с параллельностью 200.
     * Если предыдущая проверка ещё идёт — она прерывается.
     */
    public void checkAllAsync() {
        if (!starting.compareAndSet(false, true)) {
            Integer size = currentQueue == null ? 0 : currentQueue.size();
            log.info("Previous launch is not finished, remaining proxies: {}", size);
            return;
        }
        long aliveProxies = proxyRepository.getTotalAliveProxies();
        if (aliveProxies < ALLOWABLE_PROXY) {
            log.info("New proxies are needed");
            newProxyService.setNewProxy();
        }

        List<ProxyEntity> proxyEntities = proxyRepository.findAllOrderByRandom();
        List<Proxy> proxies = proxyMapper.mapToProxies(proxyEntities);

        log.info("Start proxy list sync. Size lists is {}, in total there were {}",
                proxies.size(), aliveProxies);

        if (!checkInternetConnection()) {
            log.warn("Internet connection is not available");
            proxyRepository.saveAll(proxyMapper.mapToProxyEntities(proxies));
            return;
        }

        List<Proxy> proxiesForCheck = doFirstCheck(proxies);

        log.info("Records removed from database - {}", proxies.size() - proxiesForCheck.size());

        currentQueue = new ConcurrentLinkedQueue<>(proxiesForCheck);

        Flux.fromIterable(currentQueue)
                .flatMap(proxy ->
                        checkProxyAsync(proxy)
                                .doFinally(signal -> currentQueue.remove(proxy)), THREAD_POOL)
                .subscribe(
                        proxy -> {
                        },
                        err -> log.error("Error checking proxy", err),
                        () -> {
                            log.info("Checking all proxies completed");
                            starting.set(false);
                        }
                );
    }

    private Mono<Void> checkProxyAsync(Proxy proxy) {
        HttpClient httpClient = HttpClient
                .create(connectionProvider)
                .proxy(spec -> spec.type(ProxyProvider.Proxy.SOCKS5)
                        .host(proxy.getIpAddress())
                        .port(proxy.getPort()))
                .headers(h -> {
                    h.add("User-Agent",
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) " +
                                    "AppleWebKit/537.36 (KHTML, like Gecko) " +
                                    "Chrome/124.0.0.0 Safari/537.36");
                    h.add("Accept",
                            "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
                    h.add("Accept-Language", "en-US,en;q=0.9,ru;q=0.8");
                    h.add("Accept-Encoding", "gzip, deflate, br");
                })
                .responseTimeout(Duration.ofSeconds(TIMEOUT));

        return httpClient
                .get()
                .uri(FILE_URL)
                .responseSingle((resp, body) -> {
                    if (resp.status().code() != HttpStatus.OK.value()) {
                        return Mono.error(new RuntimeException("HTTP error " + resp.status()));
                    }

                    Instant downloadStart = Instant.now();

                    return body.asByteArray()
                            .map(bytes -> downloadStart);
                })
                .flatMap(downloadStart -> Mono.fromRunnable(() -> {
                    double speed = checkSpeed(downloadStart);
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
        long duration = ChronoUnit.MICROS.between(start, now);

        double bytesPerSecond = FILE_SIZE / duration * 1_000_000;

        return bytesPerSecond / 1024.0;
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

    private List<Proxy> doFirstCheck(List<Proxy> proxies) {
        List<Proxy> proxiesForCheck = new ArrayList<>();
        List<Proxy> proxiesForRemove = new ArrayList<>();
        proxies.forEach(proxy -> {
            if (proxy.getFirstChecked() == null) {
                proxy.setFirstChecked(LocalDateTime.now());
                proxy.setAnonymity("High anonymity");
                proxy.setNumberChecks(0);
                proxy.setNumberUnansweredChecks(0);
                proxy.setUptime(0.0);
            }

            if (proxy.getPort() > 65535
                    || proxy.getUptime() == 0
                    && proxy.getNumberUnansweredChecks() > NUMBER_UNANSWERED_CHECKS
                    || Objects.nonNull(proxy.getLastSuccessfulCheck())
                    && DAYS.between(proxy.getLastSuccessfulCheck(), LocalDateTime.now()) > ONE_MONTH
                    && proxy.getUptime() < 5) {
                proxiesForRemove.add(proxy);
            } else {
                proxiesForCheck.add(proxy);
            }
        });

        if (!proxiesForRemove.isEmpty()) {
            proxyRepository.deleteAllInBatch(proxyMapper.mapToProxyEntities(proxiesForRemove));
        }

        return proxiesForCheck;
    }

}
