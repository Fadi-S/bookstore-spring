package dev.fadisarwat.bookstore.services;

import dev.fadisarwat.bookstore.dao.OauthTokenDAO;
import dev.fadisarwat.bookstore.models.OauthToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OauthTokenServiceImpl implements OauthTokenService {

    private final OauthTokenDAO tokenDAO;

    public OauthTokenServiceImpl(OauthTokenDAO tokenDAO) {
        this.tokenDAO = tokenDAO;
    }

    @Override
    @Transactional
    public void saveToken(OauthToken oauthToken) {
        tokenDAO.saveToken(oauthToken);
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
}
