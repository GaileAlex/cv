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

    @Column(name = "session_id")
    private String sessionId;

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

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "last_event")
    private LocalDateTime lastEvent;

    @Column(name = "total_time_on_site")
    private Long totalTimeOnSite;

    @Column(name = "total_visits")
    private Long totalVisits;

    @Column(name = "user_name")
    private String username;

    @JsonIgnore
    @OneToMany(mappedBy = "visitStatistics", cascade = {CascadeType.ALL})
    private List<VisitStatisticVisitDate> visitStatisticVisitDates;

    @JsonIgnore
    @OneToMany(mappedBy = "visitStatistics", cascade = {CascadeType.ALL})
    private List<VisitStatisticUserIp> visitStatisticUserIps;

    @JsonIgnore
    @OneToMany(mappedBy = "visitStatistics", cascade = {CascadeType.ALL})
    private List<VisitStatisticsEvents> visitStatisticsEvents;

}
