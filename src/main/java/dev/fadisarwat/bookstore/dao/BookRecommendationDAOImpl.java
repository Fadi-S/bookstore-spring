package dev.fadisarwat.bookstore.dao;

import dev.fadisarwat.bookstore.dto.WeightedBook;
import dev.fadisarwat.bookstore.models.Book;
import dev.fadisarwat.bookstore.models.BookBrowsed;
import dev.fadisarwat.bookstore.models.User;
import dev.fadisarwat.bookstore.services.BookRecommendationServiceImpl;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public class BookRecommendationDAOImpl implements BookRecommendationDAO {

    private final SessionFactory sessionFactory;
    BookRecommendationDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void saveBrowseHistory(Book book, User user) {
        Session session = sessionFactory.getCurrentSession();

        BookBrowsed bookBrowsed = new BookBrowsed(book, user);

        session.merge(bookBrowsed);
    }

    @Override
    public List<WeightedBook> browseHistory(User user) {
        Session session = sessionFactory.getCurrentSession();
        Query<Object[]> query = session.createQuery("select b, count(b) as weight from BookBrowsed bb join bb.book b where bb.user=:user and bb.browsedAt>=:time group by b", Object[].class);
        query.setParameter("user", user);
        Date twoWeeksAgo = new Date();
        twoWeeksAgo.setTime(twoWeeksAgo.getTime() - 14 * 24 * 60 * 60 * 1000);
        query.setParameter("time", twoWeeksAgo);

        return query.getResultList().stream().map((row) -> new WeightedBook((Book) row[0], (Long) row[1])).toList();
    }

    @Override
    public List<WeightedBook> purchaseHistory(User user) {
        Session session = sessionFactory.getCurrentSession();
        Query<Object[]> query = session.createQuery("Select bo.book, sum(bo.quantity) as weight from Order o join o.bookOrders bo where o.user=:user group by bo.book", Object[].class);
        query.setParameter("user", user);

        List<WeightedBook> books = query.getResultList().stream().map((row) -> new WeightedBook((Book) row[0], (Long) row[1])).toList();;

        books = books.stream().peek((wb) -> wb.weight *= 5).toList();

        return books;
    }
}
