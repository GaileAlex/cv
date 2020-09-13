package ee.gaile.entity.statistics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public class CommonVisitStatistics {
    @Id
    @Column(name = "id")
    @JsonIgnore
    private BigInteger id;

    @Column(name = "visit_date")
    private Date visitDate;

    @Column(name = "count_visits")
    private BigInteger countVisits;
}
