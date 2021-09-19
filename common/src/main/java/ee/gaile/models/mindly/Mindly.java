package ee.gaile.models.mindly;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
public class Mindly {

    private Long id;

    private String cryptocurrency;

    private BigDecimal amount;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfPurchase;

    private String walletLocation;

    BigDecimal currentMarketValue;

}
