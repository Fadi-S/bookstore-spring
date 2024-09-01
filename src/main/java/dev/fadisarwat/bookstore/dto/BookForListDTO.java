package dev.fadisarwat.bookstore.dto;

import dev.fadisarwat.bookstore.models.Book;

import java.io.Serializable;

public record BookForListDTO(Long id, String title, String author, String genre,
                             Long priceInPennies) implements Serializable {

    public static BookForListDTO fromBook(Book book) {
        return new BookForListDTO(book.getId(), book.getTitle(), book.getAuthor(), book.getGenre(), book.getPriceInPennies());
    }
}
