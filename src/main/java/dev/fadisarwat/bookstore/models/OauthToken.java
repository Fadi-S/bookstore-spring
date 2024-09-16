package dev.fadisarwat.bookstore.models;

import jakarta.persistence.*;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Entity
@Table(name="oauth_token")
public class OauthToken {

    public OauthToken() {
        this.token = this.generateToken();
    }

    public OauthToken(User user) {
        this.token = this.generateToken();

        this.user = user;
    }

    private String generateToken() {
        final String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        try {
            final SecureRandom secureRandom = SecureRandom.getInstanceStrong();
            final int tokenLength = 64;

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
