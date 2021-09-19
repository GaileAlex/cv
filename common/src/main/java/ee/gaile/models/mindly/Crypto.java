package ee.gaile.models.mindly;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Crypto {

    private String symbol;

    private Double lastPrice;

}
