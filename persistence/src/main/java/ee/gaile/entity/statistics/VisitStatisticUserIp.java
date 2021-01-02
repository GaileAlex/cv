package ee.gaile.entity.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "VisitStatisticUserIP")
@Table(name = "visit_statistics_user_ip")
public class VisitStatisticUserIp {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_ip")
    private String userIp;

    @ManyToOne(targetEntity = VisitStatistics.class)
    @JoinColumn(name = "visit_statistics_id")
    private VisitStatistics visitStatistics;

}
