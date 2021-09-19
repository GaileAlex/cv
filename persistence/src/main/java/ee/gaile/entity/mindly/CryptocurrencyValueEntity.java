package ee.gaile.entity.mindly;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity(name = "CryptocurrencyValues")
@Table(name = "cryptocurrency_values")
@NoArgsConstructor
@Accessors(chain = true)
public class CryptocurrencyValueEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_cryptocurrency")
    private LocalDateTime dateCryptocurrency;

    @Column(name = "value_cryptocurrency")
    private BigDecimal valueCurrency;

    @ManyToOne
    @JoinColumn(name = "bitfinex_cryptocurrency_id")
    @JsonIgnore
    private BitfinexCryptocurrencyEntity bitfinexCryptocurrency;

}
