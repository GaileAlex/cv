package ee.gaile.entity.statistics;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ManyToOne
    @JoinColumn(name = "visit_statistics_id", nullable = false)
    @JsonIgnore
    private VisitStatistics visitStatistics;

}
