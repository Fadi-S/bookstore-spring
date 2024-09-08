package dev.fadisarwat.bookstore.models;

import dev.fadisarwat.bookstore.controllers.ProfileController;
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
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(principal instanceof User) {
            return (User) principal;
        }

        return null;
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
    @Size(min = 6, message = "Password must be bigger than 6 characters")
    private String password;

    @Column
    private String picture;

    @Column(name = "stripe_id")
    private String stripeId;

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
        json.put("firstName", this.getFirstName());
        json.put("lastName", this.getLastName());
        json.put("email", this.getEmail());
        json.put("picture", this.getPicture());
        json.put("timestamp", new Date().getTime());
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

    public Long cartGeneralTotalInPennies() {
        Long subTotal = this.cartTotalPriceInPennies();
        Long tax = Math.round(subTotal * 0.14);
        Long shipping = subTotal != 0 ? 100L : 0;

        return subTotal + tax + shipping;
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

    public Boolean addToCart(Book book, Long quantity) {
        if (this.booksInCart == null) {
            this.booksInCart = new ArrayList<>();
        }

        quantity = quantity == null ? 0 : quantity;

        if(book.getQuantity() < quantity || book.getQuantity() == 0) {
            return false;
        }

        final Long q = quantity;

        this.booksInCart.stream()
                .filter(item -> item.getBook().equals(book))
                .findFirst()
                .ifPresentOrElse(
                        (item) -> item.setQuantity(q == 0 ? item.getQuantity()+1 : q),
                        () -> this.booksInCart.add(new ShoppingCartItem(book, this, q == 0 ? 1 : q))
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

        this.booksInCart.removeIf((i) -> Objects.equals(i.getId(), item.getId()));
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

    public String getPicture() {
        return getPicture(false);
    }

    public String getPicture(Boolean original) {
        if (original) return picture;

        return picture == null ? "default" : picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public static String picturePath() {
        return "src/main/resources/static/images/users";
    }

    public void updateUsing(ProfileController.UserProfile user) {
        this.setFirstName(user.firstName());
        this.setLastName(user.lastName());
        this.setEmail(user.email());
    }

    public String getStripeId() {
        return stripeId;
    }

    public void setStripeId(String stripeId) {
        this.stripeId = stripeId;
    }

    public String getFullName() {
        return this.getFirstName() + " " + this.getLastName();
    }
}
