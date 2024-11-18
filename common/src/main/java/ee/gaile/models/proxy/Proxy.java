package ee.gaile.models.proxy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author Aleksei Gaile 19-Sep-21
 */
@Getter
@Setter
public class Proxy {

    private Long id;

    private String ipAddress;

    private Integer port;

    private String protocol;

    private String country;

    private String anonymity;

    private Double speed;

    private Double uptime;

    private LocalDateTime lastChecked;

    @JsonIgnore
    private LocalDateTime firstChecked;

    @JsonIgnore
    private Integer numberChecks;

    @JsonIgnore
    private Integer numberUnansweredChecks;

    @JsonIgnore
    private LocalDateTime lastSuccessfulCheck;

}
