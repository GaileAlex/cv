package ee.gaile.service.proxy;

import ee.gaile.entity.ProxyList;
import ee.gaile.repository.ProxyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProxyListService {
    private final ProxyRepository proxyRepository;

    public List<ProxyList> getAllProxy() {
        List<ProxyList> proxyLists = proxyRepository.findAll();

        return proxyLists;
    }

}
