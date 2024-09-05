package dev.fadisarwat.bookstore.controllers;

import dev.fadisarwat.bookstore.exceptions.AuthenticationFailedException;
import dev.fadisarwat.bookstore.models.Book;
import dev.fadisarwat.bookstore.models.Review;
import dev.fadisarwat.bookstore.models.User;
import dev.fadisarwat.bookstore.services.BookService;
import dev.fadisarwat.bookstore.services.ReviewService;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor editor = new StringTrimmerEditor(true);

        binder.registerCustomEditor(String.class, editor);
    }

    private final ReviewService reviewService;
    private final BookService bookService;
    ReviewController(ReviewService reviewService, BookService bookService) {
        this.reviewService = reviewService;
        this.bookService = bookService;
    }

    @GetMapping("/{bookId}")
    public List<Review> index(@PathVariable String bookId) {
        return reviewService.getReviews(Long.parseLong(bookId));
    }

    @PostMapping("/{bookId}")
    public Review createReview(@PathVariable Long bookId, @RequestBody Review review) {
        Book book = bookService.getBook(bookId);
        if(book == null) {
            throw new NotFoundException("Book not found");
        }

        User user = User.getCurrentUser();
        Boolean purchased = this.bookService.isPurchasedByUser(bookId, user.getId());
        if(! purchased) {
            throw new AuthenticationFailedException("You can't write a review on a book you didn't purchase");
        }

        review.setBook(book);
        review.setUser(user);

        return reviewService.saveReview(review);
    }
}
