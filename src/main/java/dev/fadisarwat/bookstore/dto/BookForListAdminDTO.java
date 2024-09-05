package dev.fadisarwat.bookstore.dto;

import dev.fadisarwat.bookstore.models.Book;

import java.io.Serializable;

public record BookForListAdminDTO(Long id, String title, String author, String genre,
                                  Long priceInPennies, String cover, String overview, Long quantity) implements Serializable {

    public static BookForListAdminDTO fromBook(Book book) {
        return new BookForListAdminDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                book.getPriceInPennies(),
                book.getCover(),
                book.getOverview(),
                book.getQuantity()
        );
    }
}
