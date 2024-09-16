package dev.fadisarwat.bookstore.services;

import jakarta.transaction.Transactional;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ValidationService {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    public boolean isUnique(String table, String column, String value) {
        Session session = sessionFactory.getCurrentSession();
        return session
                .createQuery("from " + table + " where " + column + " = :value", Object.class)
                .setParameter("value", value)
                .getResultList()
                .isEmpty();
    }
}