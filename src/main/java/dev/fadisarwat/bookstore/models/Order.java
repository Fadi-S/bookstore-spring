package dev.fadisarwat.bookstore.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
@Table(name="order")
public class Order {

    public enum Status {
        PENDING("pending"),
        SHIPPED("shipped"),
        DELIVERED("delivered");

        private final String description;

        Status(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
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

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "order", orphanRemoval = true)
    private List<BookOrder> bookOrders;


    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
