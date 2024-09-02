package dev.fadisarwat.bookstore.services;

import dev.fadisarwat.bookstore.dao.ReviewDAO;
import dev.fadisarwat.bookstore.models.Review;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewDAO reviewDAO;

    public ReviewServiceImpl(ReviewDAO reviewDAO) {
        this.reviewDAO = reviewDAO;
    }

    @Override
    @Transactional
    public void saveReview(Review address) {
        reviewDAO.saveReview(address);
    }

    @Override
    @Transactional
    public Review getReview(Long id) {
        return reviewDAO.getReview(id);
    }

    @Override
    @Transactional
    public void deleteReview(Long id) {
        reviewDAO.deleteReview(id);
    }

    @Override
    @Transactional
    public List<Review> getReviews(Long bookId) {
        return reviewDAO.getReviews(bookId);
    }
}
