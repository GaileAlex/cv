package ee.gaile.models.mindly;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Crypto {
   String symbol;
   Double lastPrice;
}
