package ee.gaile.controller.statistics;

import ee.gaile.models.statistics.VisitStatisticGraph;
import ee.gaile.service.statistics.StatisticsService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static ee.gaile.service.security.SecurityConfig.API_V1_PREFIX;

@RestController
@RequestMapping(path = API_V1_PREFIX + "/statistic")
@AllArgsConstructor
public class StatisticsController {
    private final StatisticsService statisticsService;

    private static final Logger ACCESS_LOG = LoggerFactory.getLogger("access-accounting-log");

    @GetMapping(path = "/graph/fromDate/{fromDate}/toDate/{toDate}/pageSize/{pageSize}/page/{page}")
    public ResponseEntity<VisitStatisticGraph> getGraphData(@PathVariable(value = "fromDate") String fromDate,
                                                            @PathVariable(value = "toDate") String toDate,
                                                            @PathVariable(value = "pageSize") Integer pageSize,
                                                            @PathVariable(value = "page") Integer page) {
        return new ResponseEntity<>(statisticsService.getStatisticsGraph(fromDate, toDate, pageSize, page), HttpStatus.OK);
    }

    @PostMapping(path = "/user")
    @ResponseStatus(HttpStatus.OK)
    public void setUserSpy(HttpServletRequest request) {
        ACCESS_LOG.info("user name is {}, IP is {}, city is {}, country is {}",
                request.getHeader("user"),
                request.getHeader("userIP"),
                request.getHeader("userCity"),
                request.getHeader("userCountry"));

        statisticsService.setUserStatistics(request);
    }

    @PostMapping(path = "/user-out")
    @ResponseStatus(HttpStatus.OK)
    public void getUserOutDate(HttpServletRequest request) {
        ACCESS_LOG.info("user left date is {}", request.getHeader("dateOut"));

        statisticsService.setUserTotalTimeOnSite(request);
    }

}
