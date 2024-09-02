package dev.fadisarwat.bookstore.services;

import dev.fadisarwat.bookstore.models.Review;

import java.util.List;

public interface ReviewService {
    void saveReview(Review review);
    Review getReview(Long id);
    void deleteReview(Long id);
    List<Review> getReviews(Long bookId);
}
