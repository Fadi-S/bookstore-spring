package dev.fadisarwat.bookstore.models;

import jakarta.persistence.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;

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

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique=true)
    private String email;

    @Column(name = "password")
    private String password;

    @ElementCollection
    private List<SimpleGrantedAuthority> authorities;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public void setAuthorities(List<String> authorities) {
        this.authorities = new ArrayList<>();
        for (String authority : authorities) {
            this.authorities.add(new SimpleGrantedAuthority("ROLE_" + authority));
        }
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
}
