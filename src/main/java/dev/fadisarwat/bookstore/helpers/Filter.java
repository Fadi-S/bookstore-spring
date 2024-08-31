package dev.fadisarwat.bookstore.helpers;

public class Filter {
    private String field;
    private String value;
    private FilterType type;

    public Filter(String field, String value, FilterType type) {
        this.field = field;
        this.value = value;
        this.type = type;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        if(type == FilterType.FUZZY) {
            return value + "%";
        }

        return value;
    }

    public String getQuery() {
        return field + " " + type.sign + " :" + field;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public FilterType getType() {
        return type;
    }

    public String getSign() {
        return type.sign;
    }

    public void setType(FilterType type) {
        this.type = type;
    }
}
