package ee.gaile.sync.proxy;

import ee.gaile.entity.proxy.ProxyEntity;
import ee.gaile.entity.statistics.VisitStatisticsEntity;
import ee.gaile.models.statistics.VisitStatisticsTable;
import ee.gaile.repository.proxy.ProxyRepository;
import ee.gaile.repository.statistic.VisitStatisticsGraphRepository;
import ee.gaile.repository.statistic.VisitStatisticsRepository;
import ee.gaile.sync.SyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

/**
 * Service for determining unknown countries from a proxy
 *
 * @author Aleksei Gaile
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CountrySyncService implements SyncService {
    private static final String IP_INFO_URL = "http://ipinfo.io/";

    private final ProxyRepository proxyRepository;
    private final RestTemplate restTemplate;
    private final VisitStatisticsGraphRepository visitStatisticsGraphRepository;
    private final VisitStatisticsRepository visitStatisticsRepository;

    /**
     * Sets the proxy country
     * <p>
     * A ban is possible if the number of requests is exceeded.
     * Proxy check is interrupted after ten rejections
     */
    @Override
    public void sync() {
        List<ProxyEntity> proxyEntities = proxyRepository.findAllWhereCountryUnknown();
        updateCityToVisitStatistic();

        if (proxyEntities.isEmpty()) {
            return;
        }

        log.info("Start proxy country sync. Size lists is {} ", proxyEntities.size());

        int returnIfRequestIsBlocked = 0;

        for (ProxyEntity proxyEntity : proxyEntities) {
            if (proxyEntity.getCountry().equals("unknown")) {
                try {
                    Map<String, String> ipInfo = getIpInfoByIp(proxyEntity.getIpAddress());
                    proxyEntity.setCountry(ipInfo.get("country") + " " + ipInfo.get("city"));
                    proxyRepository.save(proxyEntity);
                } catch (Exception e) {
                    log.info("proxy set country error for {} {}", proxyEntity.getIpAddress(), proxyEntity.getPort());
                    returnIfRequestIsBlocked++;
                    if (returnIfRequestIsBlocked == 10) {
                        return;
                    }
                }
            }
        }
    }

    public void updateCityToVisitStatistic() {
        List<VisitStatisticsTable> tableList = visitStatisticsGraphRepository.updateCityToVisitStatistic();

        if (tableList.isEmpty()){
            return;
        }

        log.info("Start visit statistic sync. Size lists is {} ", tableList.size());

        tableList.forEach(c -> {
            VisitStatisticsEntity entity = visitStatisticsRepository.findById(c.getId()).get();
            try {
                Map<String, String> ipInfo = getIpInfoByIp(c.getUserIp());
                entity.setUserCity(ipInfo.get("city"));
                entity.setUserLocation(ipInfo.get("country"));
                visitStatisticsRepository.save(entity);
            } catch (Exception e) {
                log.info("proxy set country error for {}", c.getUserIp());
            }
        });
    }

    public Map<String, String> getIpInfoByIp(String ipAddress) {
        RequestEntity<Void> request = RequestEntity.get(IP_INFO_URL + ipAddress.replace("\"", ""))
                .accept(MediaType.APPLICATION_JSON).build();
        ParameterizedTypeReference<Map<String, String>> responseType =
                new ParameterizedTypeReference<>() {
                };

        return restTemplate.exchange(request, responseType).getBody();
    }

}
