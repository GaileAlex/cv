package ee.gaile.controller.proxy;

import ee.gaile.models.proxy.ProxyListWrapper;
import ee.gaile.service.proxy.ProxyService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static ee.gaile.service.security.SecurityConfig.API_V1_PREFIX;

@RestController
@RequestMapping(path = API_V1_PREFIX + "/proxy")
@AllArgsConstructor
public class ProxyController {
    private final ProxyService proxyService;

    @GetMapping(path = "/list/{pageSize}/{page}")
    public ResponseEntity<ProxyListWrapper> getGraphData(@PathVariable(value = "pageSize") Integer pageSize,
                                                         @PathVariable(value = "page") Integer page) {
        return new ResponseEntity<>(proxyService.getProxy(pageSize, page), HttpStatus.OK);
    }


}
