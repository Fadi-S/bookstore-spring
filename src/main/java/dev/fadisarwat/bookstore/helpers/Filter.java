package dev.fadisarwat.bookstore.helpers;

import java.util.HashMap;
import java.util.Map;

public class Filter {
    private String field;
    private String value;
    private Type type;
    private Filter orFilter;

    public enum Type {
        EQUALS("="),
        FUZZY("LIKE"),
        FULL_TEXT("FTS"),
        GREATER_THAN(">"),
        LESS_THAN("<"),
        IN("in");

        public final String sign;

        Type(String sign) {
            this.sign = sign;
        }
    }

    public static Filter of(String field, String value) {
        Type type = Type.EQUALS;
        if (value.startsWith("~")) {
            value = value.substring(1);
            type = Type.FUZZY;
        } else if (value.startsWith("|")) {
            value = value.substring(1);
            type = Type.FULL_TEXT;
        } else if (value.startsWith(">")) {
            value = value.substring(1);
            type = Type.GREATER_THAN;
        } else if (value.startsWith("<")) {
            value = value.substring(1);
            type = Type.LESS_THAN;
        } else if (value.startsWith("[]")) {
            value = value.substring(2);
            type = Type.IN;
        }

        return new Filter(field, value, type);
    }

    public Filter(String field, String value, Type type) {
        this.field = field;
        this.value = value;
        this.type = type;
    }

    public Filter(String field, String value, Type type, Filter orFilter) {
        this.field = field;
        this.value = value;
        this.type = type;
        this.orFilter = orFilter;
    }

    public String getField() {
        return field;
    }

    public String getValue() {
        if(type == Type.FUZZY) {
            return value + "%";
        }

        return value;
    }

    public String getQuery() {
        String query;
        if(type == Type.IN) {
            int values = value.split(",").length;
            StringBuilder in = new StringBuilder();
            for (int i = 0; i < values; i++) {
                in.append(":").append(field).append(i);
                if (i < values - 1) in.append(",");
            }

            query = field + " " + type.sign + " (" + in + ")";
        }else if (type == Type.FULL_TEXT) {
            query = "match(" + field + ") against ('" + escapeWildcardsForMySQL(value) + "' in boolean mode)";
        }else {
            query = field + " " + type.sign + " :" + field;
        }

        if(orFilter != null) {
            query += " or " + orFilter.getQuery();
            query = "(" + query + ")";
        }

        return query;
    }

    private String escapeStringForMySQL(String s) {
        return s.replaceAll("\\\\", "\\\\\\\\")
                .replaceAll("\b","\\b")
                .replaceAll("\n","\\n")
                .replaceAll("\r", "\\r")
                .replaceAll("\t", "\\t")
                .replaceAll("\\x1A", "\\Z")
                .replaceAll("\\x00", "\\0")
                .replaceAll("'", "\\'")
                .replaceAll("\"", "\\\"");
    }

    private String escapeWildcardsForMySQL(String s) {
        return escapeStringForMySQL(s)
                .replaceAll("%", "\\%")
                .replaceAll("_","\\_");
    }


    public Map<String, String> getParameters() {
        Map<String, String> values = new HashMap<>();

        if(type == Type.FULL_TEXT) {
            values = new HashMap<>();
        }else if(type == Type.IN) {
            String[] split = value.split(",");
            for (int i = 0; i < split.length; i++) {
                values.put(field + i, split[i]);
            }
        } else {
            values.put(field, getValue());
        }

        values.putAll(orFilter != null ? orFilter.getParameters() : Map.of());

        return values;
    }
}
