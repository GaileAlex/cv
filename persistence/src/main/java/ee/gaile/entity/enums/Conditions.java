package ee.gaile.entity.enums;

public enum Conditions {
    CONTAINS("Contains",""),
    BEGIN_WITH("Begin with",""),
    EQUAL_TO("Eqyal to", "");

    private final String name;
    private final String query;

    Conditions(String name, String query) {
        this.name = name;
        this.query = query;
    }

    public String getName() {
        return name;
    }

    public String getQuery() {
        return query;
    }


}
