package dev.fadisarwat.bookstore.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="orders")
public class Order {

    public List<BookOrder> getBookOrders() {
        return bookOrders;
    }

    public void setBookOrders(List<BookOrder> bookOrders) {
        this.bookOrders = bookOrders;
    }

    public @NotNull(message = "is required") Status getStatus() {
        return status;
    }

    public void setStatus(@NotNull(message = "is required") Status status) {
        this.status = status;
    }

    public Boolean getPaid() {
        return paid;
    }

    public void setPaid(Boolean paid) {
        this.paid = paid;
    }

    public Long getPriceInPennies() {
        return priceInPennies;
    }

    public void setPriceInPennies(@NotNull(message = "is required") @Min(1) Long priceInPennies) {
        this.priceInPennies = priceInPennies;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public enum Status {
        PENDING("Pending"),
        SHIPPED("Shipped"),
        DELIVERED("Delivered");

        private final String description;

        Status(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    public Order() {}

    public Order(User user, Address address, Long priceInPennies, Boolean paid, Status status) {
        this.user = user;
        this.address = address;
        this.priceInPennies = priceInPennies;
        this.paid = paid;
        this.status = status;
        this.number = generateNumber();
        this.createdAt = new Date();
    }

    private String generateNumber() {
        final String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        try {
            final SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            final int tokenLength = 8;

            return secureRandom
                    .ints(tokenLength, 0, chars.length())
                    .mapToObj(chars::charAt)
                    .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                    .toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;

    @Column(name = "price_in_pennies")
    @NotNull(message="is required")
    @Min(1)
    private Long priceInPennies;

    @Column(name="is_paid")
    private Boolean paid;

    @Column(name="status")
    @Enumerated(EnumType.STRING)
    @NotNull(message="is required")
    private Status status;

    @Column(name="created_at")
    private Date createdAt;

    @Column(name="number")
    private String number;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "order", orphanRemoval = true)
    private List<BookOrder> bookOrders;

    public void addBookOrder(Book book, Long quantity) {
        if (bookOrders == null) {
            bookOrders = new ArrayList<>();
        }

        BookOrder bookOrder = new BookOrder(book, this, quantity, book.getPriceInPennies());

        bookOrders.add(bookOrder);
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
