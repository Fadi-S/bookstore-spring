package dev.fadisarwat.bookstore.services;

import dev.fadisarwat.bookstore.models.OauthToken;
import dev.fadisarwat.bookstore.models.User;

import java.util.List;

public interface OauthTokenService {
    OauthToken createToken(User user);
    OauthToken getToken(String token);
    void deleteToken(String token);
    List<String> getAuthorities(OauthToken oauthToken);
}
