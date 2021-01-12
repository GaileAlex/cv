package gaile.enums;

import java.util.HashMap;
import java.util.Map;

public enum Currency {
    USD("USD", "USD");

    private final String currency;
    private final String name;

    private static final Map<String, String> CURRENCY_TYPE_MAP = new HashMap<>();

    static {
        for (Currency c : values()) {
            CURRENCY_TYPE_MAP.put(c.name, c.currency);
        }
    }

    Currency(String name, String currency) {
        this.name = name;
        this.currency = currency;
    }

    public static String getCurrency(String name) {
        return CURRENCY_TYPE_MAP.get(name);
    }
}
