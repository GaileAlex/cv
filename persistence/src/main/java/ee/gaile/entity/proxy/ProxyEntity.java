package ee.gaile.entity.proxy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "last_checked")
    private LocalDateTime lastChecked;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "first_checked")
    @JsonIgnore
    private LocalDateTime firstChecked;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "last_successful_check")
    private LocalDateTime lastSuccessfulCheck;

    @Column(name = "number_checks")
    @JsonIgnore
    private Integer numberChecks;

    @Column(name = "number_unanswered_checks")
    @JsonIgnore
    private Integer numberUnansweredChecks;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ProxyEntity that = (ProxyEntity) o;
        return Objects.equals(ipAddress, that.ipAddress) && Objects.equals(port, that.port);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ipAddress, port);
    }

}
