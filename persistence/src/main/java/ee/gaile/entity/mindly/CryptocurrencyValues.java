package ee.gaile.entity.mindly;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "CryptocurrencyValues")
@Table(name = "cryptocurrency_values")
public class CryptocurrencyValues {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "date_cryptocurrency")
    private Date dateCryptocurrency;

    @Column(name = "value_cryptocurrency")
    private BigDecimal valueCurrency;

    @ManyToOne
    @JoinColumn(name = "bitfinex_cryptocurrency_id")
    @JsonIgnore
    private BitfinexCryptocurrency bitfinexCryptocurrency;

}
