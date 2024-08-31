package dev.fadisarwat.bookstore.helpers;

public class Sort {
    private String field;
    private Direction direction;

    public enum Direction {
        ASC, DESC
    }

    public Sort(String field, Direction direction) {
        this.field = field;
        this.direction = direction;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getDirection() {
        return direction == Direction.ASC ? "ASC" : "DESC";
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }
}
