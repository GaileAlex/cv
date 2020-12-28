package ee.gaile.models.statistics;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDate;

@Getter
@Setter
public class VisitStatisticsGraph {

    private LocalDate visitDate;
    private BigInteger countVisits;
}
