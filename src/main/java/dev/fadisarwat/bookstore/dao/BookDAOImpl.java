package dev.fadisarwat.bookstore.dao;

import dev.fadisarwat.bookstore.models.Book;
import dev.fadisarwat.bookstore.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookDAOImpl implements BookDAO {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void saveBook(Book book) {
        Session session = sessionFactory.getCurrentSession();

        session.merge(book);
    }

    @Override
    public Book getBook(Long id) {
        Session session = sessionFactory.getCurrentSession();

        return session.find(Book.class, id);
    }

    @Override
    public void deleteBook(Long id) {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("delete from Book where id=:id");

        query.setParameter("id", id);

        query.executeUpdate();
    }

    public List<Book> getBooks() {
        Session session = sessionFactory.getCurrentSession();

        Query query = session.createQuery("from Book");

        return query.getResultList();
    }
}
