package ee.gaile.sync.mindly;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import ee.gaile.dto.mindly.Crypto;
import ee.gaile.entity.mindly.CryptocurrencyValues;
import ee.gaile.enums.BitfinexCryptocurrencyEnum;
import ee.gaile.enums.Currency;
import ee.gaile.repository.mindly.BitfinexCryptocurrencyRepository;
import ee.gaile.repository.mindly.CryptocurrencyValueRepository;
import ee.gaile.sync.SyncService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BitfinexAccessSyncService implements SyncService {
    private static final Logger CURRENCY_LOG = LoggerFactory.getLogger("currency-log");
    private static final Logger ERROR_LOG = LoggerFactory.getLogger("error-log");

    private static final int LAST_PRICE = 7;
    private static final String BITFINEX_URL = "https://api-pub.bitfinex.com/v2/tickers?symbols=%s";
    private static final String CURRENCY_URL = "https://api.exchangeratesapi.io/latest?symbols=";

    private final CryptocurrencyValueRepository cryptocurrencyValueRepository;
    private final BitfinexCryptocurrencyRepository bitfinexCryptocurrencyRepository;

    @Override
    public void sync() {
        Map<String, String> map = BitfinexCryptocurrencyEnum.getCurrencyName();

        map.forEach((k, v) -> {
            String urlBitfinex = BITFINEX_URL.replace("%s", v);
            try {
                BigDecimal price;
                Crypto crypto = getDataByUrl(urlBitfinex);

                //Ripple not sold for euros, but it is not accurate. We receive in dollars and we translate in euro at the rate.
                if (k.equals("Ripple")) {
                    BigDecimal usdEur = new BigDecimal(getCurrencyUrl(CURRENCY_URL + Currency.getCurrency("USD"))
                            .replaceAll(".*?([\\d.]+).*", "$1"));
                    price = BigDecimal.valueOf(crypto.getLastPrice())
                            .divide(usdEur, 2, BigDecimal.ROUND_HALF_UP);
                } else {
                    price = BigDecimal.valueOf(crypto.getLastPrice());
                }

                CryptocurrencyValues cryptocurrencyValues = new CryptocurrencyValues();
                cryptocurrencyValues.setDateCryptocurrency(LocalDateTime.now());
                cryptocurrencyValues.setValueCurrency(price);

                cryptocurrencyValues.setBitfinexCryptocurrency(bitfinexCryptocurrencyRepository
                        .findByCryptocurrencyName(k).orElseThrow(NullPointerException::new));

                cryptocurrencyValueRepository.save(cryptocurrencyValues);

                CURRENCY_LOG.info("The current {} rate is {} €", k, price);
            } catch (Exception e) {
                ERROR_LOG.error("Error getting currency data {} - {}", k, e.getMessage());
            }
        });

    }

    private Crypto getDataByUrl(String url) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36" +
                " (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        ResponseEntity<List<String[]>> listResponseEntity = restTemplate.exchange(url, HttpMethod.GET, entity,
                new ParameterizedTypeReference<List<String[]>>() {
                });

        return new Crypto(Objects.requireNonNull(listResponseEntity.getBody()).get(0)[0],
                Double.valueOf(listResponseEntity.getBody().get(0)[LAST_PRICE]));
    }

    private String getCurrencyUrl(String url) throws IOException {
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

