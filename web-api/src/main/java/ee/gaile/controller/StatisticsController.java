package ee.gaile.controller;

import ee.gaile.models.statistics.VisitStatisticGraph;
import ee.gaile.service.statistics.StatisticsService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static ee.gaile.service.security.SecurityConfig.API_V1_PREFIX;

@RestController
@RequestMapping(path = API_V1_PREFIX + "/statistic")
@AllArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    private static final Logger ACCESS_LOG = LoggerFactory.getLogger("access-accounting-log");

    @GetMapping(path = "/graph", produces = "application/json")
    public VisitStatisticGraph getGraphData() {
        return statisticsService.getStatisticsGraph();
    }

    @PostMapping(path = "/user", produces = "application/json")
    public void getUserSpy(HttpServletRequest request) {
        for (String header : IP_HEADER_CANDIDATES) {
            String ip = request.getHeader(header);
            ACCESS_LOG.info("user name is {} is {}",
                    ip, request.getRemoteAddr());

            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                ACCESS_LOG.info("user name is {} is {}",
                        ip, request.getRemoteAddr());
            }
        }

        ACCESS_LOG.info("user name is {} IP is {}, city is {}, country is {}, Remote is {} ",
                request.getHeader("user"), request.getHeader("userIP"), request.getHeader("userCity"),
                request.getHeader("userCountry"), request.getHeader("X-Real-IP"));
        statisticsService.setUserStatistics(request);
    }

    private static final String[] IP_HEADER_CANDIDATES = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "X-Real-IP",
            "HTTP_X_REAL_IP",
            "REMOTE_ADDR"};

}
