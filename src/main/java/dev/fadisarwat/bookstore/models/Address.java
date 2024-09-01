package dev.fadisarwat.bookstore.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name="address")
public class Address {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name="full_name")
    @NotNull(message="is required")
    private String fullName;

    @Column
    @NotNull(message="is required")
    @Size(min = 2, max = 2, message = "must be 2 characters")
    private String country;

    @Column(name="address1")
    @NotNull(message="is required")
    private String street1;

    @Column(name="address2")
    private String street2;

    @Column
    @NotNull(message="is required")
    private String city;


    @Column(name="postal_code")
    private String postalCode;

    public Address() {}

    public Address(String fullName, String country, String street1, String street2, String city, String postalCode) {
        this.fullName = fullName;
        this.country = country;
        this.street1 = street1;
        this.street2 = street2;
        this.city = city;
        this.postalCode = postalCode;
    }

    public void setFromAddress(Address address) {
        this.fullName = address.getFullName();
        this.country = address.getCountry();
        this.street1 = address.getStreet1();
        this.street2 = address.getStreet2();
        this.city = address.getCity();
        this.postalCode = address.getPostalCode();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getStreet1() {
        return street1;
    }

    public void setStreet1(String street1) {
        this.street1 = street1;
    }

    public String getStreet2() {
        return street2;
    }

    public void setStreet2(String street2) {
        this.street2 = street2;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String toString() {
        return fullName + "\n" + street1 + " " + street2 + "\n" + city + ", " + postalCode + "\n" + country;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
