package ee.gaile.models.statistics;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class VisitStatisticsTable {

    private String userLocation;
    private LocalDateTime firstVisit;
    private LocalDateTime lastVisit;
    private Long totalVisits;
    private String userName;
    private String userIp;
    private String userCity;
    private Long totalTimeOnSite;
}
