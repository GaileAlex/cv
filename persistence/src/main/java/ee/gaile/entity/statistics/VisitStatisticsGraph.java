package ee.gaile.entity.statistics;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.Date;

@Getter
@Setter
public class VisitStatisticsGraph {
    private Date visitDate;
    private BigInteger countVisits;
}
