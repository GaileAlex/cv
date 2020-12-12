package ee.gaile.models.statistics;

import ee.gaile.entity.statistics.VisitStatisticsGraph;
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
    private List<VisitStatisticsGraph> countedVisit;
    private List<VisitStatisticsGraph> visitStatisticsNewUsers;
}
