package dev.fadisarwat.bookstore.models;

import dev.fadisarwat.bookstore.annotations.Unique;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UniqueElements;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "user")
public class User {

    public User() {}

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
    @NotNull(message="is required")
    @Size(min=1, message="is required")
    private String firstName;

    @Column(name = "last_name")
    @NotNull(message="is required")
    @Size(min=1, message="is required")
    private String lastName;

    @Column(name = "email", unique=true)
    @NotNull(message="is required")
    @Size(min=1, message="is required")
    @Email
//    @Unique(value="email", table="User")
    private String email;

    @Column(name = "password")
    @NotNull(message="is required")
    @Size(min=1, message="is required")
    private String password;

    @ElementCollection
    @CollectionTable(name = "authorities", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "authority")
    private List<String> authorities;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
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

    public void addBookToCart(ShoppingCartItem book) {
        if (this.booksInCart == null) {
            this.booksInCart = new ArrayList<>();
        }

        // Check if the book is already in the cart
        for (ShoppingCartItem item : this.booksInCart) {
            if (item.getBook().getId().equals(book.getBook().getId())) {
                item.setQuantity(item.getQuantity() + 1);
                return;
            }
        }

        this.booksInCart.add(book);
    }
}
