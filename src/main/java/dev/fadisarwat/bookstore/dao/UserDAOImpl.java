package dev.fadisarwat.bookstore.dao;

import dev.fadisarwat.bookstore.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO {

    private final SessionFactory sessionFactory;

    UserDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void saveUser(User user) {
        Session session = sessionFactory.getCurrentSession();

        session.merge(user);
    }

    @Override
    public User getUser(String email) {
        Session session = sessionFactory.getCurrentSession();

        Query<User> query = session.createQuery("from User where email=:email", User.class);

        query.setParameter("email", email);

        return query.getResultList().stream().findFirst().orElse(null);
    }

    @Override
    public User getUser(Long id) {
        Session session = sessionFactory.getCurrentSession();

        return session.find(User.class, id);
    }

    @Override
    public void deleteUser(Long id) {
        Session session = sessionFactory.getCurrentSession();

        MutationQuery query = session.createMutationQuery("delete from User where id=:id");

        query.setParameter("id", id);

        query.executeUpdate();
    }

    public List<User> getUsers() {
        Session session = sessionFactory.getCurrentSession();

        Query<User> query = session.createQuery("from User", User.class);

        return query.getResultList();
    }
}
