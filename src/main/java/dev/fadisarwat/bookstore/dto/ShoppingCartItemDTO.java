package dev.fadisarwat.bookstore.dto;

import dev.fadisarwat.bookstore.models.Book;
import dev.fadisarwat.bookstore.models.ShoppingCartItem;

public class ShoppingCartItemDTO {
    private final Long quantity;
    private final BookForListDTO book;

    public ShoppingCartItemDTO(Book book, Long quantity) {
        this.book = BookForListDTO.fromBook(book);
        this.quantity = quantity;
    }

    public static ShoppingCartItemDTO fromShoppingCartItem(ShoppingCartItem item) {
        return new ShoppingCartItemDTO(item.getBook(), item.getQuantity());
    }

    public Long getId() {
        return book.id();
    }

    public String getTitle() {
        return book.title();
    }

    public String getAuthor() {
        return book.author();
    }

    public String getGenre() {
        return book.genre();
    }

    public Long getPriceInPennies() {
        return book.priceInPennies();
    }

    public Long getQuantity() {
        return quantity;
    }

    public Long totalPriceInPennies() {
        return book.priceInPennies() * quantity;
    }

    public String getCover() {
        return book.cover();
    }
}
