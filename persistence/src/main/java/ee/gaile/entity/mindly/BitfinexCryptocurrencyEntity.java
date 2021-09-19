package ee.gaile.entity.mindly;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.List;

@Setter
@Getter
@Entity(name = "BitfinexCryptocurrency")
@Table(name = "bitfinex_cryptocurrency")
@Accessors(chain = true)
@NoArgsConstructor
public class BitfinexCryptocurrencyEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cryptocurrency_name")
    private String cryptocurrencyName;

    @OneToMany(mappedBy = "bitfinexCryptocurrency", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<CryptocurrencyValueEntity> cryptocurrencyValues;

}
