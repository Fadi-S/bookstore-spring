package dev.fadisarwat.bookstore.services;

import dev.fadisarwat.bookstore.dao.OauthTokenDAO;
import dev.fadisarwat.bookstore.models.OauthToken;
import dev.fadisarwat.bookstore.models.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OauthTokenServiceImpl implements OauthTokenService {

    private final OauthTokenDAO tokenDAO;

    public OauthTokenServiceImpl(OauthTokenDAO tokenDAO) {
        this.tokenDAO = tokenDAO;
    }

    @Override
    @Transactional
    public OauthToken createToken(User user) {
        return tokenDAO.createToken(user);
    }

    @Override
    @Transactional
    public OauthToken getToken(String token) {
        return tokenDAO.getToken(token);
    }

    @Override
    @Transactional
    public void deleteToken(String token) {
        tokenDAO.deleteToken(token);
    }

    @Override
    @Transactional
    public List<String> getAuthorities(OauthToken oauthToken) {
        return tokenDAO.getAuthorities(oauthToken);
    }
}
