package dev.fadisarwat.bookstore.services;

import dev.fadisarwat.bookstore.models.Review;

import java.util.List;

public interface ReviewService {
    Review saveReview(Review review);
    Boolean wroteReview(Long bookId, Long userId);
    Review getReview(Long id);
    void deleteReview(Long id);
    List<Review> getReviews(Long bookId);
}
