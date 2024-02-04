package ee.gaile.entity.statistics;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
@Entity(name = "VisitStatisticsEvents")
@Table(name = "visit_statistic_events")
public class VisitStatisticsEventsEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "events")
    private String events;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @ManyToOne(targetEntity = VisitStatisticsEntity.class)
    @JoinColumn(name = "visit_statistics_id")
    private VisitStatisticsEntity visitStatistics;

}
