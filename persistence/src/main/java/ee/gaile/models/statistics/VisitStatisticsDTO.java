package ee.gaile.models.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitStatisticsDTO {
    private Long id;
    private String userIP;
    private String userLocation;
    private Date firstVisit;
    private Date lastVisit;
    private Long totalVisits;
    private String username;

}
