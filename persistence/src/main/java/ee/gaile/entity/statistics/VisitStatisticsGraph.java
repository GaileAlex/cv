package ee.gaile.entity.statistics;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.time.LocalDateTime;

@Getter
@Setter
public class VisitStatisticsGraph {

    private LocalDateTime visitDate;
    private BigInteger countVisits;
}
