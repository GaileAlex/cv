package ee.gaile.models.statistics;

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
    private List<VisitStatisticsDTO> visitStatisticsDTOList;

    private List<VisitStatisticsUser> countedVisit;
}
