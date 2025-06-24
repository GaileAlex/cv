package ee.gaile.sync.proxy;

import ee.gaile.entity.proxy.ProxyEntity;
import ee.gaile.entity.proxy.ProxySiteEntity;
import ee.gaile.repository.proxy.ProxyRepository;
import ee.gaile.repository.proxy.ProxySitesRepository;
import ee.gaile.sync.SyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
public class NewProxyService implements SyncService {
    private final ProxyRepository proxyRepository;
    private final ProxySitesRepository proxySitesRepository;

    /**
     * Searches for new proxies on sites, adds to the database
     */
    public void sync() {
        int siteConnectionError = 0;
        int counter = 0;

        List<ProxySiteEntity> proxySites = proxySitesRepository.findAll();

        for (ProxySiteEntity proxySite : proxySites) {
            List<ProxyEntity> proxyEntities = new ArrayList<>();
            try {
                Document doc = Jsoup.connect(proxySite.getUrl())
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                                "(KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36")
                        .referrer("https://www.google.com")
                        .timeout(10000).get();

                proxyEntities.addAll(addProxyOnSeparatedIpAndPort(doc));

                if (proxyEntities.isEmpty()) {
                    proxyEntities.addAll(addProxyByIpAndPort(doc));
                }
                if (proxyEntities.isEmpty()) {
                    log.warn("No proxies received from the site - {} ", proxySite.getUrl());
                    continue;
                }

                counter += saveProxies(proxyEntities);

            } catch (IOException | NumberFormatException e) {
                log.warn("Site connection error - {} ", proxySite.getUrl());
                siteConnectionError = siteConnectionError + 1;
            }
        }
        log.warn("Total site connection errors {} ", siteConnectionError);
        log.info("Proxy saved - {}", counter);
    }

    /**
     * Adds proxy by IP and port from the given document.
     *
     * @param  doc   the document to extract proxy information from
     * @return      a list of ProxyEntity objects containing the extracted proxy information
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
     * @param  doc	the Document to extract proxies from
     * @return     	a list of ProxyEntity objects containing separated IP and port proxies
     */
    private List<ProxyEntity> addProxyOnSeparatedIpAndPort(Document doc) {
        List<ProxyEntity> proxyEntities = new ArrayList<>();
        Elements table = doc.select("table");
        Elements rows = table.select("tr");

        for (int i = 1; i < rows.size(); i++) {
            Element row = rows.get(i);
            String[] parts = row.toString().split("[^0-9.0-9]");
            ProxyEntity proxyEntity = new ProxyEntity();

            for (String ip : parts) {

                if (ip.matches("^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.)" +
                        "{3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$")) {
                    proxyEntity.setIpAddress(ip);
                    continue;
                }
                if (Objects.nonNull(proxyEntity.getIpAddress()) && !ip.isEmpty()) {
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
     * @param  proxyEntities  the list of proxy entities to save
     * @return                the number of successfully saved proxy entities
     */
    private int saveProxies(List<ProxyEntity> proxyEntities) {
        int count = 0;

        for (ProxyEntity proxyEntity : proxyEntities) {
            try {
                proxyRepository.save(proxyEntity);
                count++;
            } catch (Exception ignored) {
                // Ignore the error of adding an existing proxy to the database
            }
        }

        return count;
    }

}
