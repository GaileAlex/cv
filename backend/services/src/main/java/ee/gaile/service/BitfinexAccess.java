package ee.gaile.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;

/**
 * receiving and parsing requests with Bitfinex
 */
public class BitfinexAccess {
    private String currency;

    public BitfinexAccess(String currency) {
        this.currency = currency;
    }

    /**
     * @return currency value
     * @throws IOException
     */
    public BigDecimal getPrice() throws IOException {
        switch (currency) {
            case ("Bitcoin"):
                currency = "BTCEUR";
                break;
            case ("Ethereum"):
                currency = "ETHEUR";
                break;
            case ("Ripple"):
                currency = "XRPUSD";
                break;
        }
        String urlBitfinex = "https://api.bitfinex.com/v1/book/" + currency;
        BigDecimal bitfinexRate = new BigDecimal(urlGetData(urlBitfinex)
                .replaceAll(".*?([\\d.]+).*", "$1"));

        //Ripple not sold for euros, but it is not accurate. We receive in dollars and we translate in euro at the rate.
        if (currency.equals("XRPUSD")) {
            BigDecimal usdEur = new BigDecimal(urlGetData("https://api.exchangeratesapi.io/latest?symbols=USD")
                    .replaceAll(".*?([\\d.]+).*", "$1"));

            bitfinexRate = bitfinexRate.divide(usdEur, 2, BigDecimal.ROUND_HALF_UP);
        }

        return bitfinexRate;
    }

    /**
     * @param url site
     * @return raw response from site
     * @throws IOException
     */
    private String urlGetData(String url) throws IOException {
        URL surl = new URL(url);
        URLConnection request = surl.openConnection();
        request.connect();

        JsonParser jp = new JsonParser();
        JsonElement element = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        JsonObject obj = element.getAsJsonObject();

        return obj.toString();
    }
}

