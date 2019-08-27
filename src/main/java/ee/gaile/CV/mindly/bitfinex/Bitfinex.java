package ee.gaile.CV.mindly.bitfinex;

import java.io.IOException;
import java.math.BigDecimal;

public interface Bitfinex {
    BigDecimal getPrice() throws IOException;
}
