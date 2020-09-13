package ee.gaile.models.statistics;

import ee.gaile.entity.statistics.VisitStatisticsNewUser;
import ee.gaile.entity.statistics.VisitStatisticsUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitStatisticGraph {

    private List<VisitStatisticsDTO> visitStatisticsDTOS;

    private List<VisitStatisticsUser> countedVisit;

    private List<VisitStatisticsNewUser> visitStatisticsNewUsers;
}
