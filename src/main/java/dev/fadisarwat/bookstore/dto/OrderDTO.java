package dev.fadisarwat.bookstore.dto;

import dev.fadisarwat.bookstore.models.Book;
import dev.fadisarwat.bookstore.models.BookOrder;
import dev.fadisarwat.bookstore.models.Order;

import java.util.Date;
import java.util.List;

public class OrderDTO {

    private final Long priceInPennies;
    private final Long id;
    private final String status;
    private final List<BookForListDTO> books;
    private final Date createdAt;
    private final Boolean isPaid;
    private final String number;

    public OrderDTO(Long id, Long priceInPennies, String status, Date createdAt, Boolean isPaid, List<BookOrder> bookOrders, String number) {
        this.id = id;
        this.priceInPennies = priceInPennies;
        this.createdAt = createdAt;
        this.status = status;
        this.isPaid = isPaid;
        this.number = number;

        this.books = bookOrders.stream().map((bookOrder) -> {
            Book book = bookOrder.getBook();
            book.setPriceInPennies(bookOrder.getPriceInPennies());
            book.setQuantity(bookOrder.getQuantity());
            return BookForListDTO.fromBook(book);
        }).toList();
    }

    public static OrderDTO fromOrder(Order order) {
        return new OrderDTO(
                order.getId(),
                order.getPriceInPennies(),
                order.getStatus().getDescription(),
                order.getCreatedAt(),
                order.getPaid(),
                order.getBookOrders(),
                order.getNumber()
        );

    }

    public Long getPriceInPennies() {
        return priceInPennies;
    }

    public String getStatus() {
        return status;
    }

    public List<BookForListDTO> getBooks() {
        return books;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Boolean getPaid() {
        return isPaid;
    }

    public String getNumber() {
        return number;
    }

    public Long getId() {
        return id;
    }
}
