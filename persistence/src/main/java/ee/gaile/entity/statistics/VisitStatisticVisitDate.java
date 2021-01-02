package ee.gaile.entity.statistics;

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
@Entity(name = "VisitStatisticVisitDate")
@Table(name = "visit_statistic_visit_date")
public class VisitStatisticVisitDate {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "visit_date")
    private LocalDateTime visitDate;

    @ManyToOne(targetEntity = VisitStatistics.class)
    @JoinColumn(name = "visit_statistics_id")
    private VisitStatistics visitStatistics;

}
