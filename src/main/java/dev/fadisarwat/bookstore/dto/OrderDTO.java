package dev.fadisarwat.bookstore.dto;

import dev.fadisarwat.bookstore.models.BookOrder;
import dev.fadisarwat.bookstore.models.Order;

import java.util.Date;
import java.util.List;

public class OrderDTO {

    private Long priceInPennies;
    private Order.Status status;
    private List<BookForListDTO> books;
    private Date createdAt;
    private Boolean isPaid;

    public OrderDTO(Long priceInPennies, Order.Status status, Date createdAt, Boolean isPaid, List<BookOrder> bookOrders) {
        this.priceInPennies = priceInPennies;
        this.createdAt = createdAt;
        this.status = status;
        this.isPaid = isPaid;

        this.books = bookOrders.stream().map((bookOrder) -> BookForListDTO.fromBook(bookOrder.getBook())).toList();
    }

    public static OrderDTO fromOrder(Order order) {
        return new OrderDTO(
                order.getPriceInPennies(),
                order.getStatus(),
                order.getCreatedAt(),
                order.getPaid(),
                order.getBookOrders()
        );

    }

    public Long getPriceInPennies() {
        return priceInPennies;
    }

    public Order.Status getStatus() {
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
}
