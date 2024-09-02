package dev.fadisarwat.bookstore.dao;

import dev.fadisarwat.bookstore.models.Review;

import java.util.List;

public interface ReviewDAO {
    void saveReview(Review review);
    Review getReview(Long id);
    List<Review> getReviews(Long bookId);
    void deleteReview(Long id);
}
