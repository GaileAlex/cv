package ee.gaile.entity.statistics;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Entity(name = "VisitStatistics")
@Table(name = "visit_statistics",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"user_name"})})
public class VisitStatistics {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_location")
    private String userLocation;

    @Column(name = "user_city")
    private String userCity;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "first_visit")
    private LocalDateTime firstVisit;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "last_visit")
    private LocalDateTime lastVisit;

    @Column(name = "total_time_on_site")
    private Long totalTimeOnSite;

    @Column(name = "total_visits")
    private Long totalVisits;

    @Column(name = "user_name")
    private String username;

    @JsonIgnore
    @OneToMany(mappedBy = "visitStatistics", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<VisitStatisticVisitDate> visitStatisticVisitDates;

    @JsonIgnore
    @OneToMany(mappedBy = "visitStatistics", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<VisitStatisticUserIp> visitStatisticUserIps;

}
