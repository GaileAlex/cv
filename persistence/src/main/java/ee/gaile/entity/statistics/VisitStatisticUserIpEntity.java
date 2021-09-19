package ee.gaile.entity.statistics;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Accessors(chain = true)
@Entity(name = "VisitStatisticUserIP")
@Table(name = "visit_statistics_user_ip")
public class VisitStatisticUserIpEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_ip")
    private String userIp;

    @ManyToOne(targetEntity = VisitStatisticsEntity.class)
    @JoinColumn(name = "visit_statistics_id")
    private VisitStatisticsEntity visitStatistics;

}
