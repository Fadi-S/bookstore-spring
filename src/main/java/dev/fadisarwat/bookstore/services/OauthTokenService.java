package dev.fadisarwat.bookstore.services;

import dev.fadisarwat.bookstore.models.OauthToken;

public interface OauthTokenService {
    void saveToken(OauthToken oauthToken);
    OauthToken getToken(String token);
    void deleteToken(String token);
}
