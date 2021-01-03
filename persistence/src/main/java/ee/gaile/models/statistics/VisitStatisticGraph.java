package ee.gaile.models.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitStatisticGraph {

    private List<BigInteger> newUsers;
    private List<BigInteger> totalVisits;
    private List<LocalDate> dates;
    private List<VisitStatisticsTable> visitStatisticsTables;
    private Long total;
}
