package ee.gaile.entity.statistics;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
@Entity(name = "VisitStatisticVisitDate")
@Table(name = "visit_statistic_visit_date")
public class VisitStatisticVisitDateEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "visit_date")
    private LocalDateTime visitDate;

    @ManyToOne(targetEntity = VisitStatisticsEntity.class)
    @JoinColumn(name = "visit_statistics_id")
    private VisitStatisticsEntity visitStatistics;

}
