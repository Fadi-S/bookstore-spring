package dev.fadisarwat.bookstore.dao;

import dev.fadisarwat.bookstore.models.Review;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ReviewDAOImpl implements ReviewDAO {

    private final SessionFactory sessionFactory;

    ReviewDAOImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Review saveReview(Review review) {
        Session session = sessionFactory.getCurrentSession();

        return session.merge(review);
    }

    @Override
    public Boolean wroteReview(Long bookId, Long userId) {
        Session session = sessionFactory.getCurrentSession();

        Query<Long> query = session.createNativeQuery("SELECT id from review where user_id=:userId and book_id=:bookId", Long.class);
        query.setParameter("bookId", bookId);
        query.setParameter("userId", userId);

        return ! query.getResultList().isEmpty();
    }

    @Override
    public Review getReview(Long id) {
        Session session = sessionFactory.getCurrentSession();

        return session.find(Review.class, id);
    }

    @Override
    public void deleteReview(Long id) {
        Session session = sessionFactory.getCurrentSession();

        MutationQuery query = session.createMutationQuery("delete from Review where id=:id");

        query.setParameter("id", id);

        query.executeUpdate();
    }

    @Override
    public List<Review> getReviews(Long bookId) {
        Session session = sessionFactory.getCurrentSession();

        Query<Review> query = session.createNativeQuery("Select * from review where book_id=:bookId", Review.class);

        query.setParameter("bookId", bookId);

        return query.getResultList();
    }
}
