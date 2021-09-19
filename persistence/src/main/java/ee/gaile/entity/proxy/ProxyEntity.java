package ee.gaile.entity.proxy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity(name = "ProxyList")
@Table(name = "proxy_list")
@NoArgsConstructor
public class ProxyEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private Long id;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "port")
    private Integer port;

    @Column(name = "protocol")
    private String protocol;

    @Column(name = "country")
    private String country;

    @Column(name = "anonymity")
    private String anonymity;

    @Column(name = "speed")
    private Double speed;

    @Column(name = "uptime")
    private Double uptime;

    @Column(name = "response")
    private Long response;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "last_checked")
    private LocalDateTime lastChecked;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "first_checked")
    @JsonIgnore
    private LocalDateTime firstChecked;

    @Column(name = "number_checks")
    @JsonIgnore
    private Integer numberChecks;

    @Column(name = "number_unanswered_checks")
    @JsonIgnore
    private Integer numberUnansweredChecks;
}
