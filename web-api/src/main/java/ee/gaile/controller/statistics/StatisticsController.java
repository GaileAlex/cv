package ee.gaile.controller.statistics;

import ee.gaile.models.statistics.VisitStatisticGraph;
import ee.gaile.service.statistics.StatisticsService;
import ee.gaile.service.statistics.UserEvents;
import ee.gaile.service.statistics.UserStatisticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * REST service controller for receiving and sending site visit data
 *
 * @author Aleksei Gaile
 */
@RestController
@RequestMapping(path = "/statistic")
@RequiredArgsConstructor
@Tag(name = "StatisticsController", description = "Controller for working with site visit statistics")
public class StatisticsController {
    private static final Logger ACCESS_LOG = LoggerFactory.getLogger("access-accounting-log");

    private final StatisticsService statisticsService;
    private final UserStatisticsService userStatisticsService;
    private final UserEvents userEvents;

    @GetMapping(path = "/graph/fromDate/{fromDate}/toDate/{toDate}/pageSize/{pageSize}/page/{page}")
    @Operation(summary = "Service for displaying data for a chart")
    public ResponseEntity<VisitStatisticGraph> getGraphData(@PathVariable(value = "fromDate") String fromDate,
                                                            @PathVariable(value = "toDate") String toDate,
                                                            @PathVariable(value = "pageSize") Integer pageSize,
                                                            @PathVariable(value = "page") Integer page) {
        return new ResponseEntity<>(statisticsService.getStatisticsGraph(fromDate, toDate, pageSize, page), HttpStatus.OK);
    }

    @PostMapping(path = "/user")
    @Operation(summary = "Service for saving data about the user's visit to the site")
    public ResponseEntity<Map<String, String>> setUserSpy(HttpServletRequest request) {
        ACCESS_LOG.info("********************************************************************************************");
        ACCESS_LOG.info("user IP is {}, city is {}, country is {}, ID: {}",
                request.getHeader("userIP"),
                request.getHeader("userCity"),
                request.getHeader("userCountry"),
                request.getHeader("userId"));
        ACCESS_LOG.info("********************************************************************************************");

        return new ResponseEntity<>(userStatisticsService.setStatistics(request), HttpStatus.OK);
    }

    @PostMapping(path = "/events")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Service for saving data about user actions on the site")
    public void setEventUser(HttpServletRequest request) {
        ACCESS_LOG.info("user event: {}, ID: {}",
                request.getHeader("events"),
                request.getHeader("userId"));

        userEvents.setUserEvent(request);
    }

    @PostMapping(path = "/user-out")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Service for saving the time that the user spent on the site")
    public void setUserOutDate(HttpServletRequest request) {
        ACCESS_LOG.info("user left date is {}", request.getHeader("dateOut"));

        userEvents.setUserTotalTimeOnSite(request);
    }

    @GetMapping(path = "/file")
    public ResponseEntity<byte[]> getFile() throws IOException {
        InputStream in = getClass()
                .getResourceAsStream("/1M.iso");
        return new ResponseEntity<>(IOUtils.toByteArray(in), HttpStatus.OK);
    }

}
