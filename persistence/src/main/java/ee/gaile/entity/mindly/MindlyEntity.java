package ee.gaile.entity.mindly;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@Entity(name = "Mindly")
@Table(name = "mindly")
@NoArgsConstructor
public class MindlyEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "mindly_crypto_currency")
    private String cryptocurrency;

    @Column(name = "mindly_amount")
    private BigDecimal amount;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "mindly_date_of_purchase")
    private LocalDate dateOfPurchase;

    @Column(name = "mindly_wallet_location")
    private String walletLocation;

    @Transient
    BigDecimal currentMarketValue;
}
