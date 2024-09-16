package dev.fadisarwat.bookstore.dto;

import dev.fadisarwat.bookstore.models.*;

import java.util.Date;
import java.util.List;

public class OrderDTOForAdmin {

    private final Long id;
    private final Long priceInPennies;
    private final String status;
    private final List<BookForListAdminDTO> books;
    private final Address address;
    private final Date createdAt;
    private final Boolean isPaid;
    private final String number;
    private final User user;

    public OrderDTOForAdmin(Long id, Long priceInPennies, String status, Date createdAt, Boolean isPaid, List<BookOrder> bookOrders, String number, User user, Address address) {
        this.id = id;
        this.priceInPennies = priceInPennies;
        this.createdAt = createdAt;
        this.status = status;
        this.isPaid = isPaid;
        this.number = number;
        this.user = user;
        this.address = address;

        this.books = bookOrders.stream().map((bookOrder) -> {
            Book book = bookOrder.getBook();
            book.setPriceInPennies(bookOrder.getPriceInPennies());
            book.setQuantity(bookOrder.getQuantity());
            return BookForListAdminDTO.fromBook(book);
        }).toList();
    }

    public static OrderDTOForAdmin fromOrder(Order order) {
        return new OrderDTOForAdmin(
                order.getId(),
                order.getPriceInPennies(),
                order.getStatus().getDescription(),
                order.getCreatedAt(),
                order.getPaid(),
                order.getBookOrders(),
                order.getNumber(),
                order.getUser(),
                order.getAddress()
        );

    }

    public Long getId() {
        return id;
    }
    public Long getPriceInPennies() {
        return priceInPennies;
    }

    public String getStatus() {
        return status;
    }

    public List<BookForListAdminDTO> getBooks() {
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

    public AddressDTO getAddress() { return AddressDTO.fromAddress(address); }

}
