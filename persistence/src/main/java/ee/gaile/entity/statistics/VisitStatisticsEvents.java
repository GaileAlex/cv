package ee.gaile.entity.statistics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity(name = "VisitStatisticsEvents")
@Table(name = "visit_statistic_events")
public class VisitStatisticsEvents {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "events")
    private String events;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @ManyToOne(targetEntity = VisitStatistics.class)
    @JoinColumn(name = "visit_statistics_id")
    private VisitStatistics visitStatistics;

}
