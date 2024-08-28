package dev.fadisarwat.bookstore.dao;

import dev.fadisarwat.bookstore.models.OauthToken;

public interface OauthTokenDAO {
    void saveToken(OauthToken oauthToken);
    OauthToken getToken(String token);
    void deleteToken(String token);
}
