package ee.gaile.controller.proxy;

import ee.gaile.models.proxy.ProxyListWrapper;
import ee.gaile.service.proxy.ProxyService;
import ee.gaile.sync.proxy.NewProxyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static ee.gaile.service.security.SecurityConfig.API_V1_PREFIX;

/**
 * REST service controller to get proxy list table data
 *
 * @author Aleksei Gaile
 */
@RestController
@RequestMapping(path = API_V1_PREFIX + "/proxy")
@RequiredArgsConstructor
@Tag(name = "ProxyController", description = "Controller for getting proxies")
public class ProxyController {
    private final ProxyService proxyService;
    private final NewProxyService newProxyService;

    @GetMapping(path = "/list/{pageSize}/{page}")
    @Operation(summary = "Service for displaying a list of proxies")
    public ResponseEntity<ProxyListWrapper> getGraphData(@PathVariable(value = "pageSize") Integer pageSize,
                                                         @PathVariable(value = "page") Integer page) {
        return new ResponseEntity<>(proxyService.getProxy(pageSize, page), HttpStatus.OK);
    }

    @PostMapping()
    @Operation(summary = "Start synchronize proxies")
    public ResponseEntity<Void> startChecking() {
        proxyService.start();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/add-new")
    @Operation(summary = "Add new proxies")
    public ResponseEntity<Void> setNewProxy() {
        newProxyService.setNewProxy();
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
