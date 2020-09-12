package ee.gaile.entity.statistics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;
import java.util.Date;

@Data
@Builder
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class VisitStatisticsUser {
    @Id
    @Column(name = "id")
    @JsonIgnore
    private BigInteger id;

    @Column(name = "visit_date")
    private Date visitDate;

    @Column(name = "count_visits")
    private BigInteger countVisits;
}
