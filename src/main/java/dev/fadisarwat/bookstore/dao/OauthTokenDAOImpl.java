package dev.fadisarwat.bookstore.dao;

import dev.fadisarwat.bookstore.models.OauthToken;
import dev.fadisarwat.bookstore.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OauthTokenDAOImpl implements OauthTokenDAO {

    private final SessionFactory sessionFactory;
    OauthTokenDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public OauthToken createToken(User user) {
        Session session = sessionFactory.getCurrentSession();

        OauthToken oauthToken = new OauthToken(user);

        session.merge(oauthToken);

        return oauthToken;
    }

    @Override
    public OauthToken getToken(String token) {
        Session session = sessionFactory.getCurrentSession();

        Query<OauthToken> query = session.createQuery("Select o from OauthToken o join fetch o.user u join fetch u.authorities where token=:token", OauthToken.class);
        query.setParameter("token", token);

         return query.getSingleResultOrNull();
    }

    @Override
    public void deleteToken(String token) {
        Session session = sessionFactory.getCurrentSession();

        MutationQuery query = session.createMutationQuery("delete from OauthToken where token=:token");

        query.setParameter("token", token);

        query.executeUpdate();
    }

    public List<String> getAuthorities(OauthToken oauthToken) {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createNativeQuery("select authority from authorities where user_id=:id");

        query.setParameter("id", oauthToken.getUser().getId());

        return query.getResultList();
    }
}
