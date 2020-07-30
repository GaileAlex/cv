package ee.gaile.service.mindly;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * receiving and parsing requests with Bitfinex
 */
@Service
@EnableScheduling
public class BitfinexAccessService {
    private static final Logger CURRENCY_LOG = LoggerFactory.getLogger("currency-log");

    private final HashMap<String, BigDecimal> currencyMap = new HashMap<>();

    /**
     * @throws IOException getDataByUrl generate exception
     */
    @Scheduled(fixedRate = 600000)
    private void setPrice() throws IOException {
        currencyMap.put("Ethereum", getPrice("Ethereum"));
        currencyMap.put("Ripple", getPrice("Ripple"));
        currencyMap.put("Bitcoin", getPrice("Bitcoin"));
        CURRENCY_LOG.info("The current Ethereum rate is {} €", currencyMap.get("Ethereum"));
        CURRENCY_LOG.info("The current Ripple rate is {} €", currencyMap.get("Ripple"));
        CURRENCY_LOG.info("The current Bitcoin rate is {} €", currencyMap.get("Bitcoin"));
    }

    private BigDecimal getPrice(String currency) throws IOException {
        BigDecimal bitfinexRate;

        switch (currency) {
            case ("Ethereum"):
                currency = "ETHEUR";
                break;
            case ("Ripple"):
                currency = "XRPUSD";
                break;
            default:
                currency = "BTCEUR";
                break;
        }
        String urlBitfinex = "https://api.bitfinex.com/v1/book/" + currency;

        try {
            bitfinexRate = new BigDecimal(getDataByUrl(urlBitfinex)
                    .replaceAll(".*?([\\d.]+).*", "$1"));
        } catch (IOException e) {
            bitfinexRate = new BigDecimal(0);
            e.printStackTrace();
        }


        //Ripple not sold for euros, but it is not accurate. We receive in dollars and we translate in euro at the rate.
        if (currency.equals("XRPUSD")) {
            BigDecimal usdEur = new BigDecimal(getDataByUrl("https://api.exchangeratesapi.io/latest?symbols=USD")
                    .replaceAll(".*?([\\d.]+).*", "$1"));

            bitfinexRate = bitfinexRate.divide(usdEur, 2, BigDecimal.ROUND_HALF_UP);
        }

        return bitfinexRate;
    }

    /**
     * @param url site
     * @return raw response from site
     * @throws IOException URLConnection
     */
    private String getDataByUrl(String url) throws IOException {
        URL surl = new URL(url);
        URLConnection request = surl.openConnection();
        request.connect();

        JsonParser jp = new JsonParser();
        JsonElement element = jp.parse(new InputStreamReader((InputStream) request.getContent(), StandardCharsets.UTF_8));
        JsonObject obj = element.getAsJsonObject();

        return obj.toString();
    }

    public BigDecimal getCurrency(String currency) {
        return currencyMap.get(currency);
    }
}

