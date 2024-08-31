package dev.fadisarwat.bookstore.helpers;

public enum FilterType {
    EQUALS("="),
    FUZZY("LIKE"),
    FULL_TEXT("FTS"),
    GREATER_THAN(">"),
    LESS_THAN("<");

    public final String sign;

    FilterType(String sign) {
        this.sign = sign;
    }
}
