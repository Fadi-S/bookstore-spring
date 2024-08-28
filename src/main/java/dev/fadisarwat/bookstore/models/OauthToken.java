package dev.fadisarwat.bookstore.models;

import jakarta.persistence.*;

@Entity
@Table(name="oauth_token")
public class OauthToken {

    @Id
    private String token;

    @ManyToOne(fetch = FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name="user_id")
    private User user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
