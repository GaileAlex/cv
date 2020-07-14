package ee.gaile.entity.enums;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum Conditions {
    AUTHOR("Author", " book_article "),
    TITLE("Title", " book_title "),
    DATE("Date", ""),
    CONTAINS("Contains", " ilike "),
    BEGIN_WITH("Begin with", " ilike "),
    EQUAL_TO("Equal to", " = "),
    ALL_CONDITIONS("allConditions", " and "),
    AT_LEAST_ONE_CONDITION("At least one condition", " or "),
    NONE_OF_THE_CONDITION("None of the condition", "");

    private final String query;
    private final String name;

    private static final Map<String, String> conditionTypeMap=new HashMap<>();

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
