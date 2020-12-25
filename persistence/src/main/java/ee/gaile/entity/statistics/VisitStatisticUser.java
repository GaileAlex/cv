package ee.gaile.entity.statistics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "VisitStatisticUser")
@Table(name = "visit_statistic_user")
public class VisitStatisticUser {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "visit_date")
    private LocalDateTime visitDate;

    @ManyToOne
    @JoinColumn(name = "visit_statistics_id", nullable = false)
    @JsonIgnore
    private VisitStatistics visitStatistics;

}
