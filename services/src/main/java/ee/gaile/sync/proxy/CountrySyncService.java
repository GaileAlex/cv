package ee.gaile.sync.proxy;

import ee.gaile.entity.proxy.ProxyList;
import ee.gaile.repository.proxy.ProxyRepository;
import ee.gaile.sync.SyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CountrySyncService implements SyncService {
    private static final Logger ERROR_LOG = LoggerFactory.getLogger("error-log");

    private final ProxyRepository proxyRepository;

    @Override
    public void sync() {
        RestTemplate restTemplate = new RestTemplate();
        List<ProxyList> proxyLists = proxyRepository.findAllWhereCountryUnknown();

        if (proxyLists.size() == 0) {
            return;
        }

        int returnIfRequestIsBlocked = 0;

        for (ProxyList proxyList : proxyLists) {
            if (proxyList.getCountry().equals("unknown")) {
                try {
                    ResponseEntity<String> listResponseEntity2 = restTemplate
                            .exchange("http://ipinfo.io/" + proxyList.getIpAddress(), HttpMethod.GET, null,
                                    new ParameterizedTypeReference<String>() {
                                    });
                    String[] st = listResponseEntity2.getBody().split("\",\\n");

                    String city = st[3].split(": \"")[1] + " " + st[2].split(": \"")[1];
                    proxyList.setCountry(city);
                    proxyRepository.save(proxyList);

                } catch (Exception e) {
                    ERROR_LOG.info("proxy set country error for {} {}", proxyList.getIpAddress(), proxyList.getPort());
                    returnIfRequestIsBlocked++;
                    if (returnIfRequestIsBlocked == 10) {
                        return;
                    }
                }
            }
        }
    }
}
