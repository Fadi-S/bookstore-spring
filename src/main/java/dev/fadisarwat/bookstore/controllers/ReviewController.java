package dev.fadisarwat.bookstore.controllers;

import dev.fadisarwat.bookstore.models.Review;
import dev.fadisarwat.bookstore.services.ReviewService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;
    ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/{bookId}")
    public List<Review> index(@PathVariable String bookId) {
        return reviewService.getReviews(Long.parseLong(bookId));
    }
}
