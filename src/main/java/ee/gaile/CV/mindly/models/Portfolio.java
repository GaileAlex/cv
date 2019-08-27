package ee.gaile.CV.mindly.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "mindly")
public class Portfolio {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String cryptocurrency;

    @NotNull(message = "Amount name is required")
    @NumberFormat
    private BigDecimal amount;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dateOfPurchase;

    @NotBlank(message = "Wallet location is required")
    private String walletLocation;


}
