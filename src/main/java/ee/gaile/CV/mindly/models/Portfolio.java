package ee.gaile.CV.mindly.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mindly")
public class Portfolio {

    @Id
    @Column(name = "mindly_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid2")
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "mindly_crypto_currency")
    private String cryptocurrency;

    @NotNull(message = "Amount name is required")
    @NumberFormat
    @Column(name = "mindly_amount", length = 20)
    private BigDecimal amount;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "mindly_date_of_purchase")
    private Date dateOfPurchase;

    @NotBlank(message = "Wallet location is required")
    @Column(name = "mindly_walletLocation", length = 100)
    private String walletLocation;
}
