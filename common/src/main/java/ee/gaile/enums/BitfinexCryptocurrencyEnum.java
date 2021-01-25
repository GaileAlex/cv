package ee.gaile.enums;

import java.util.HashMap;
import java.util.Map;

public enum BitfinexCryptocurrencyEnum {
    ETHEREUM("Ethereum", "tETHEUR"),
    RIPPLE("Ripple", "tXRPUSD"),
    BITCOIN("Bitcoin", "tBTCEUR");

    private final String currency;
    private final String name;

    private static final Map<String, String> CRYPTOCURRENCY_TYPE_MAP = new HashMap<>();

    static {
        for (BitfinexCryptocurrencyEnum c : values()) {
            CRYPTOCURRENCY_TYPE_MAP.put(c.name, c.currency);
        }
    }

    BitfinexCryptocurrencyEnum(String name, String currency) {
        this.name = name;
        this.currency = currency;
    }

    public static Map<String, String> getCurrencyName() {
        return CRYPTOCURRENCY_TYPE_MAP;
    }


}
