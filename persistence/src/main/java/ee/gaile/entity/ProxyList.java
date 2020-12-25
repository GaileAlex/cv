package ee.gaile.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ProxyList")
@Table(name = "proxy_list")
public class ProxyList {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    private Integer speed;

    @Column(name = "uptime")
    private Double uptime;

    @Column(name = "response")
    private Integer response;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "last_checked")
    private LocalDateTime lastChecked;

    @Column(name = "number_checks")
    private Integer numberChecks;
}
