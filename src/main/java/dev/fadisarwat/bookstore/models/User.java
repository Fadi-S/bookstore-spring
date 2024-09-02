package dev.fadisarwat.bookstore.models;

import dev.fadisarwat.bookstore.dto.ShoppingCartItemDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

@Entity
@Table(name = "user")
public class User {

    public User() {
    }

    public User(String firstName, String lastName, String email, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.setPassword(password);
    }

    public static User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private String firstName;

    @Column(name = "last_name")
    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private String lastName;

    @Column(name = "email", unique = true)
    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    @Email
//    @Unique(value="email", table="User")
    private String email;

    @Column(name = "password")
    @NotNull(message = "is required")
    @Size(min = 1, message = "is required")
    private String password;

    @ElementCollection
    @CollectionTable(name = "authorities", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "authority")
    private List<String> authorities;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private List<Address> addresses;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private List<ShoppingCartItem> booksInCart;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (String authority : this.authorities) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + authority));
        }
        return authorities;
    }

    public List<String> getAuthoritiesString() {
        return this.authorities;
    }

    public Map<String, Object> get() {
        HashMap<String, Object> json = new HashMap<>();
        json.put("firstName", this.firstName);
        json.put("lastName", this.lastName);
        json.put("email", this.email);
        return json;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = authorities;
    }

    public void addAuthority(String authority) {
        if (this.authorities == null) {
            this.authorities = new ArrayList<>();
        }

        this.authorities.add(authority);
    }

    public Long cartTotalPriceInPennies() {
        return booksInCart
                .stream()
                .map(ShoppingCartItemDTO::fromShoppingCartItem)
                .reduce(0L, (acc, item) -> acc + item.totalPriceInPennies(), Long::sum);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = new BCryptPasswordEncoder().encode(password);
    }

    public Boolean matchPassword(String password) {
        return new BCryptPasswordEncoder().matches(password, this.getPassword());
    }

    public List<ShoppingCartItem> getBooksInCart() {
        return booksInCart;
    }

    public void setBooksInCart(List<ShoppingCartItem> booksInCart) {
        this.booksInCart = booksInCart;
    }

    public Boolean addToCart(Book book) {
        if (this.booksInCart == null) {
            this.booksInCart = new ArrayList<>();
        }

        if(book.getQuantity() == 0) {
            return false;
        }

        this.booksInCart.stream()
                .filter(item -> item.getBook().equals(book))
                .findFirst()
                .ifPresentOrElse(
                        ShoppingCartItem::incrementQuantity,
                        () -> this.booksInCart.add(new ShoppingCartItem(book, this, 1L))
                );

        return true;
    }

    public void removeFromCart(Book book) {
        if (this.booksInCart == null) {
            return;
        }

        ShoppingCartItem item = this.booksInCart
                .stream()
                .filter(i -> i.getBook().equals(book))
                .findFirst()
                .orElse(null);

        if(item == null) {
            return;
        }

        if(item.getQuantity() > 1) {
            item.decrementQuantity();
        } else {
            this.booksInCart.remove(item);
        }
    }

    public void emptyCart() {
        this.booksInCart.removeIf(i -> true);
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public void addAddress(Address address) {
        if (this.addresses == null) {
            this.addresses = new ArrayList<>();
        }

        address.setUser(this);

        this.addresses.add(address);
    }
}
