package dev.fadisarwat.bookstore.dto;

import dev.fadisarwat.bookstore.models.Book;

public class WeightedBook {
    public Book book;
    public double weight;

    public WeightedBook(Book book, double weight) {
        this.book = book;
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "WeightedBook{" +
                "book=" + book +
                ", weight=" + weight +
                '}';
    }
}
