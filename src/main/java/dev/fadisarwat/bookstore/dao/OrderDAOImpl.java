package dev.fadisarwat.bookstore.dao;

import dev.fadisarwat.bookstore.models.Order;
import dev.fadisarwat.bookstore.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDAOImpl implements OrderDAO {

    private final SessionFactory sessionFactory;

    OrderDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void saveOrder(Order order) {
        Session session = sessionFactory.getCurrentSession();

        session.merge(order);
    }

    @Override
    public Order getOrder(Long id) {
        Session session = sessionFactory.getCurrentSession();

        return session.find(Order.class, id);
    }

    @Override
    public void deleteOrder(Long id) {
        Session session = sessionFactory.getCurrentSession();

        MutationQuery query = session.createMutationQuery("delete from Order where id=:id");

        query.setParameter("id", id);

        query.executeUpdate();
    }

    @Override
    public List<Order> getOrders() {
        Session session = sessionFactory.getCurrentSession();

        Query<Order> query = session.createQuery("from Order", Order.class);

        return query.getResultList();
    }

    @Override
    public List<Order> getOrdersPaginated(int page, int size) {
        Session session = sessionFactory.getCurrentSession();

        Query<Order> query = session.createQuery("from Order order by id desc", Order.class);

        if (page > 0 && size > 0) {
            query.setFirstResult((page - 1) * size);
            query.setMaxResults(size);
        }

        return query.getResultList();
    }

    @Override
    public List<Order> getOrders(User user) {
        Session session = sessionFactory.getCurrentSession();

        Query<Order> query = session.createQuery("Select o from Order o join fetch o.bookOrders bo join fetch bo.book b where user=:user order by o.id desc", Order.class);
        query.setParameter("user", user);

        return query.getResultList();
    }
}
