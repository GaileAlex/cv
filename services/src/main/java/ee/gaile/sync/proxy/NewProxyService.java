package ee.gaile.sync.proxy;

import ee.gaile.entity.proxy.ProxyEntity;
import ee.gaile.entity.proxy.ProxySiteEntity;
import ee.gaile.repository.proxy.ProxyRepository;
import ee.gaile.repository.proxy.ProxySitesRepository;
import lombok.AllArgsConstructor;
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

/**
 * Service for adding new proxies to DB
 *
 * @author Aleksei Gaile
 */
@Slf4j
@Service
@AllArgsConstructor
public class NewProxyService {
    private final ProxyRepository proxyRepository;
    private final ProxySitesRepository proxySitesRepository;

    /**
     * Searches for new proxies on sites, adds to the database
     */
    public void setNewProxy() {
        List<ProxyEntity> proxyEntities = new ArrayList<>();

        List<ProxySiteEntity> proxySites = proxySitesRepository.findAll();

        for (ProxySiteEntity proxySite : proxySites) {
            try {
                Document doc = Jsoup.connect(proxySite.getUrl())
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                                "(KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36")
                        .referrer("https://www.google.com")
                        .timeout(10000).get();

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
                        if (Objects.nonNull(proxyEntity.getIpAddress()) && !ip.equals("")) {
                            proxyEntity.setPort(Integer.valueOf(ip));
                            proxyEntity.setProtocol("SOCKS5");
                            proxyEntity.setCountry("unknown");
                            proxyEntities.add(proxyEntity);
                            break;
                        }
                    }
                }
            } catch (IOException | NumberFormatException e) {
                log.warn("Site connection error {} ", proxySite.getUrl());
            }
        }

        int counter = 0;
        for (ProxyEntity proxyEntity : proxyEntities) {
            // Ignore the error of adding an existing proxy to the database
            try {
                proxyRepository.save(proxyEntity);
                counter++;
            } catch (Exception ignored) {
            }
        }

        log.info("Proxy saved - {}", counter);
    }

}
