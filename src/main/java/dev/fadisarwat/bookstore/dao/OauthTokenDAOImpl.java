package dev.fadisarwat.bookstore.dao;

import dev.fadisarwat.bookstore.models.OauthToken;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class OauthTokenDAOImpl implements OauthTokenDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void saveToken(OauthToken oauthToken) {
        Session session = sessionFactory.getCurrentSession();

        session.merge(oauthToken);
    }

    @Override
    public OauthToken getToken(String token) {
        Session session = sessionFactory.getCurrentSession();

        return session.find(OauthToken.class, token);
    }

    @Override
    public void deleteToken(String token) {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("delete from OauthToken where id=:token");

        query.setParameter("token", token);

        query.executeUpdate();
    }
}
