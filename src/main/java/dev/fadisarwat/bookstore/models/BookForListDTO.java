package dev.fadisarwat.bookstore.models;

import java.io.Serializable;

public class BookForListDTO implements Serializable {
    private Long id;
    private String title;
    private String author;
    private String genre;
    private Long priceInPennies;

    public BookForListDTO() {
    }

    public BookForListDTO(Long id, String title, String author, String genre, Long priceInPennies) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.priceInPennies = priceInPennies;
    }

    public static BookForListDTO fromBook(Book book) {
        return new BookForListDTO(book.getId(), book.getTitle(), book.getAuthor(), book.getGenre(), book.getPriceInPennies());
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getGenre() {
        return genre;
    }

    public Long getPriceInPennies() {
        return priceInPennies;
    }
}
