package dev.fadisarwat.bookstore.models;

import jakarta.persistence.*;

@Table(name="book_order")
@Entity
public class BookOrder {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column
    private Long quantity;

    @Column(name="price_in_pennies")
    private Long priceInPennies;

    public BookOrder() {}

    public BookOrder(Book book, Order order, Long quantity, Long priceInPennies) {
        this.book = book;
        this.order = order;
        this.quantity = quantity;
        this.priceInPennies = priceInPennies;
    }

    public void returnBook() {
        this.book.returnBook(this.quantity);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Long getPriceInPennies() {
        return priceInPennies;
    }

    public void setPriceInPennies(Long priceInPennies) {
        this.priceInPennies = priceInPennies;
    }
}
