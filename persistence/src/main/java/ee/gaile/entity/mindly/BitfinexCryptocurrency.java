package ee.gaile.entity.mindly;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "BitfinexCryptocurrency")
@Table(name = "bitfinex_cryptocurrency")
public class BitfinexCryptocurrency {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cryptocurrency_name")
    private String cryptocurrencyName;

    @OneToMany(mappedBy = "bitfinexCryptocurrency", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<CryptocurrencyValues> cryptocurrencyValues;

}
