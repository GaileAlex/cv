package ee.gaile.sync.proxy;

import ee.gaile.entity.proxy.ProxyEntity;
import ee.gaile.entity.proxy.ProxySiteEntity;
import ee.gaile.repository.proxy.ProxyRepository;
import ee.gaile.repository.proxy.ProxySitesRepository;
import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service for adding new proxies to DB
 *
 * @author Aleksei Gaile
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class NewProxyService {
    private final ProxyRepository proxyRepository;
    private final ProxySitesRepository proxySitesRepository;

    /**
     * Searches for new proxies on sites, adds to the database
     */
    @Async
    public void setNewProxy() {
        int siteConnectionError = 0;

        List<ProxySiteEntity> proxySites = proxySitesRepository.findAll();
        Set<ProxyEntity> proxyEntities = new HashSet<>();

        for (ProxySiteEntity proxySite : proxySites) {
            WebDriverManager.chromedriver().avoidFallback().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless=new");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");

            WebDriver driver = new ChromeDriver(options);

            try {
                driver.get(proxySite.getUrl());
                String renderedHtml = driver.getPageSource();
                Document doc = Jsoup.parse(renderedHtml);

                List<ProxyEntity> newProxy = addProxyOnSeparatedIpAndPort(doc);

                if (newProxy.isEmpty()) {
                    proxyEntities.addAll(addProxyByIpAndPort(doc));
                } else {
                    proxyEntities.addAll(newProxy);
                }
                if (proxyEntities.isEmpty()) {
                    log.warn("No proxies received from the site - {} ", proxySite.getUrl());
                    continue;
                }

            } catch (Exception e) {
                log.warn("Site connection error - {} ", proxySite.getUrl());
                siteConnectionError = siteConnectionError + 1;
            } finally {
                driver.quit();
            }
        }
        int counter = saveProxies(proxyEntities);
        log.warn("Total site connection errors {} ", siteConnectionError);
        log.info("Proxy saved - {}", counter);
    }

    /**
     * Adds proxy by IP and port from the given document.
     *
     * @param doc the document to extract proxy information from
     * @return a list of ProxyEntity objects containing the extracted proxy information
     */
    private List<ProxyEntity> addProxyByIpAndPort(Document doc) {
        List<ProxyEntity> proxyEntities = new ArrayList<>();

        Pattern p = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}(?::\\d{1,5})");
        Matcher m = p.matcher(doc.toString());

        while (m.find()) {
            String[] address = m.group(0).split(":");
            ProxyEntity proxyEntity = new ProxyEntity();
            proxyEntity.setIpAddress(address[0]);
            proxyEntity.setPort(Integer.valueOf(address[1]));
            proxyEntity.setProtocol("SOCKS5");
            proxyEntity.setCountry("unknown");

            proxyEntities.add(proxyEntity);
        }

        return proxyEntities;
    }

    /**
     * Adds proxies with separated IP and port from the given Document.
     *
     * @param doc the Document to extract proxies from
     * @return a list of ProxyEntity objects containing separated IP and port proxies
     */
    private List<ProxyEntity> addProxyOnSeparatedIpAndPort(Document doc) {
        List<ProxyEntity> proxyEntities = new ArrayList<>();
        Elements table = doc.select("table");
        Elements rows = table.select("tr");
        Elements rowsTd = table.select("td");
        rows.addAll(rowsTd);

        for (int i = 0; i < rows.size(); i++) {
            Element row = rows.get(i);
            String[] parts = row.toString().split("[^0-9.0-9]");
            ProxyEntity proxyEntity = new ProxyEntity();

            for (String ip : parts) {

                if (ip.matches("^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.)" +
                        "{3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$")) {
                    proxyEntity.setIpAddress(ip);
                    continue;
                }
                if (Objects.nonNull(proxyEntity.getIpAddress()) && !ip.isEmpty() && ip.matches("-?\\d+")) {
                    proxyEntity.setPort(Integer.valueOf(ip));
                    proxyEntity.setProtocol("SOCKS5");
                    proxyEntity.setCountry("unknown");

                    proxyEntities.add(proxyEntity);
                    break;
                }
            }
        }

        return proxyEntities;
    }

    /**
     * Save a list of proxy entities to the database, ignoring any existing proxies.
     *
     * @param proxyEntities the list of proxy entities to save
     * @return the number of successfully saved proxy entities
     */
    private int saveProxies(Set<ProxyEntity> proxyEntities) {
        List<ProxyEntity> newEntities = proxyEntities.stream()
                .filter(e -> !proxyRepository.existsByIpAddressAndPort(e.getIpAddress(), e.getPort()))
                .toList();
        proxyRepository.saveAll(newEntities);

        return newEntities.size();
    }

}
