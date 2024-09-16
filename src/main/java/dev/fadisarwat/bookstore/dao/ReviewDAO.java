package dev.fadisarwat.bookstore.dao;

import dev.fadisarwat.bookstore.models.Review;

import java.util.List;

public interface ReviewDAO {
    Review saveReview(Review review);
    Boolean wroteReview(Long bookId, Long userId);
    Review getReview(Long id);
    List<Review> getReviews(Long bookId);
    void deleteReview(Long id);
}
