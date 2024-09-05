package dev.fadisarwat.bookstore.dto;

import dev.fadisarwat.bookstore.models.Book;
import dev.fadisarwat.bookstore.models.BookOrder;
import dev.fadisarwat.bookstore.models.Order;
import dev.fadisarwat.bookstore.models.User;

import java.util.Date;
import java.util.List;

public class OrderDTOForAdmin {

    private final Long priceInPennies;
    private final String status;
    private final List<BookForListDTO> books;
    private final Date createdAt;
    private final Boolean isPaid;
    private final String number;
    private final User user;

    public OrderDTOForAdmin(Long priceInPennies, String status, Date createdAt, Boolean isPaid, List<BookOrder> bookOrders, String number, User user) {
        this.priceInPennies = priceInPennies;
        this.createdAt = createdAt;
        this.status = status;
        this.isPaid = isPaid;
        this.number = number;
        this.user = user;

        this.books = bookOrders.stream().map((bookOrder) -> {
            Book book = bookOrder.getBook();
            book.setPriceInPennies(bookOrder.getPriceInPennies());
            book.setQuantity(bookOrder.getQuantity());
            return BookForListDTO.fromBook(book);
        }).toList();
    }

    public static OrderDTOForAdmin fromOrder(Order order) {
        return new OrderDTOForAdmin(
                order.getPriceInPennies(),
                order.getStatus().getDescription(),
                order.getCreatedAt(),
                order.getPaid(),
                order.getBookOrders(),
                order.getNumber(),
                order.getUser()
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

    public UserDTO getUser() { return UserDTO.fromUser(user); }
}
