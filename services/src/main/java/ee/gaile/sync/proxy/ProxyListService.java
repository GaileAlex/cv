package ee.gaile.sync.proxy;

import ee.gaile.sync.SyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Aleksei Gaile
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProxyListService implements SyncService {

    private final ProxyCheckSyncService proxyCheckSyncService;

    /**
     * The start of the proxy check service
     */
    @Override
    public void sync() {
        proxyCheckSyncService.checkAllAsync();
    }

}
