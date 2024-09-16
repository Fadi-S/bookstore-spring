package dev.fadisarwat.bookstore.dao;

import dev.fadisarwat.bookstore.models.Address;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AddressDAOImpl implements AddressDAO {

    private final SessionFactory sessionFactory;

    AddressDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void saveAddress(Address address) {
        Session session = sessionFactory.getCurrentSession();

        session.merge(address);
    }

    @Override
    public Address getAddress(Long id) {
        Session session = sessionFactory.getCurrentSession();

        return session.find(Address.class, id);
    }

    @Override
    public void deleteAddress(Long id) {
        Session session = sessionFactory.getCurrentSession();

        MutationQuery query = session.createMutationQuery("delete from Address where id=:id");

        query.setParameter("id", id);

        query.executeUpdate();
    }

    @Override
    public List<Address> getAddresses(Long userId) {
        Session session = sessionFactory.getCurrentSession();

        Query<Address> query = session.createQuery("from Address where user=:userId", Address.class);

        return query.getResultList();
    }
}
