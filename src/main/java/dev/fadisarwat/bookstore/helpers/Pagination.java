package dev.fadisarwat.bookstore.helpers;

import java.util.List;
import java.util.function.Function;

public class Pagination<T> {
    private final int page;
    private final int size;
    private final long totalPages;
    private final long totalElements;
    private final List<T> elements;

    public Pagination(int page, int size, long totalElements, List<T> elements) {
        this.page = page;
        this.size = size;
        this.totalPages = Math.ceilDiv(totalElements, size);
        this.totalElements = totalElements;
        this.elements = elements;
    }

    public <R> Pagination<R> mapElements(Function<T, R> mapper) {
        return new Pagination<>(page, size, totalElements, elements.stream().map(mapper).toList());
    }

    public int getPage() {
        return page;
    }

    public int getSize() {
        return size;
    }

    public long getTotalPages() {
        return totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public List<T> getElements() {
        return elements;
    }
}
