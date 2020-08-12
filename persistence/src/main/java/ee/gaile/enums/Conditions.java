package ee.gaile.enums;

import java.util.HashMap;
import java.util.Map;

public enum Conditions {
    AUTHOR("Author", " book_article "),
    TITLE("Title", " book_title "),
    DATE("Date", " book_date "),
    CONTAINS("Contains", " ilike  '%' || '"),
    BEGIN_WITH("Begin with", " ilike '"),
    DATE_FROM("Date from", " >= '"),
    EQUAL_TO("Equal to", " = '"),
    ALL_CONDITIONS("allConditions", " and "),
    AT_LEAST_ONE_CONDITION("atLeastOneCondition", " or ");

    private final String query;
    private final String name;

    private static final Map<String, String> conditionTypeMap = new HashMap<>();

    static {
        for (Conditions c : values()) {
            conditionTypeMap.put(c.name, c.query);
        }
    }

    Conditions(String name, String query) {
        this.name = name;
        this.query = query;
    }

    public static String getQuery(String name) {
        return conditionTypeMap.get(name);
    }
}
