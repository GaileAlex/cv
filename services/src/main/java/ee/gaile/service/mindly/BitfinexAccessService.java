package ee.gaile.service.mindly;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ee.gaile.entity.mindly.CryptocurrencyValues;
import ee.gaile.enums.BitfinexCryptocurrencyEnum;
import ee.gaile.enums.Currency;
import ee.gaile.repository.mindly.BitfinexCryptocurrencyRepository;
import ee.gaile.repository.mindly.CryptocurrencyValueRepository;
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
import java.util.Date;
import java.util.Map;

/**
 * receiving and parsing requests with Bitfinex
 */
@Service
@EnableScheduling
public class BitfinexAccessService {
    private static final Logger CURRENCY_LOG = LoggerFactory.getLogger("currency-log");
    private static final Logger ERROR_LOG = LoggerFactory.getLogger("error-log");

    private static final int LAST_PRICE = 7;
    private static final String BITFINEX_URL = "https://api-pub.bitfinex.com/v2/tickers?symbols=%s";
    private static final String CURRENCY_URL = "https://api.exchangeratesapi.io/latest?symbols=";

    private final CryptocurrencyValueRepository cryptocurrencyValueRepository;
    private final BitfinexCryptocurrencyRepository bitfinexCryptocurrencyRepository;

    public BitfinexAccessService(CryptocurrencyValueRepository cryptocurrencyValueRepository,
                                 BitfinexCryptocurrencyRepository bitfinexCryptocurrencyRepository) {
        this.cryptocurrencyValueRepository = cryptocurrencyValueRepository;
        this.bitfinexCryptocurrencyRepository = bitfinexCryptocurrencyRepository;
    }

    @Scheduled(fixedDelay = Long.MAX_VALUE)
    public void firstStartSyncService()  {
        setCryptocurrency();
    }

    @Scheduled(cron = "${bitfinex.access.scheduled}")
    private void setCryptocurrency()  {
        Map<String, String> map = BitfinexCryptocurrencyEnum.getCurrencyName();

        map.forEach((k, v) -> {
            String urlBitfinex = BITFINEX_URL.replace("%s", v);
            try {
                BigDecimal price;

                String dataFromUrl = getDataByUrl(urlBitfinex).split(",")[LAST_PRICE];

                //Ripple not sold for euros, but it is not accurate. We receive in dollars and we translate in euro at the rate.
                if (k.equals("XRPUSD")) {
                    BigDecimal usdEur = new BigDecimal(getDataByUrl(CURRENCY_URL + Currency.getCurrency("USD"))
                            .replaceAll(".*?([\\d.]+).*", "$1"));

                    price = new BigDecimal(dataFromUrl)
                            .divide(usdEur, 2, BigDecimal.ROUND_HALF_UP);
                } else {
                    price = new BigDecimal(dataFromUrl);
                }

                CryptocurrencyValues cryptocurrencyValues = new CryptocurrencyValues();
                cryptocurrencyValues.setDateCryptocurrency(new Date());
                cryptocurrencyValues.setValueCurrency(price);

                cryptocurrencyValues.setBitfinexCryptocurrency(bitfinexCryptocurrencyRepository
                        .findByCryptocurrencyName(k).get());

                cryptocurrencyValueRepository.save(cryptocurrencyValues);

                CURRENCY_LOG.info("The current {} rate is {} €", k, price);
            } catch (IOException e) {
                ERROR_LOG.error("Error getting currency data {} - {}", k, e.getMessage());
            }
        });

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

        if (url.contains("exchangeratesapi")) {
            JsonObject obj = element.getAsJsonObject();
            return obj.toString();
        }

        JsonArray obj = element.getAsJsonArray();

        return obj.toString();
    }

}

