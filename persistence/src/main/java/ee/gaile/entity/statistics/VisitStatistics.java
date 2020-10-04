package ee.gaile.entity.statistics;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "VisitStatistics")
@Table(name = "visit_statistics",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_name"})})
public class VisitStatistics {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_ip")
    private String userIP;

    @Column(name = "user_location")
    private String userLocation;

    @Column(name = "user_city")
    private String userCity;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "first_visit")
    private Date firstVisit;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "last_visit")
    private Date lastVisit;

    @Column(name = "total_visits")
    private Long totalVisits;

    @Column(name = "user_name")
    private String username;

    @OneToMany(mappedBy = "visitStatistics", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<VisitStatisticUser> visitStatisticUsers;

}
