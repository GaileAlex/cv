package ee.gaile.entity.mindly;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "Mindly")
@Table(name = "mindly")
public class Mindly {

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
