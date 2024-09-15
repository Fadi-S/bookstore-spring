package dev.fadisarwat.bookstore.controllers;

import dev.fadisarwat.bookstore.helpers.Filter;
import dev.fadisarwat.bookstore.helpers.Pagination;
import dev.fadisarwat.bookstore.helpers.Sort;
import dev.fadisarwat.bookstore.models.Book;
import dev.fadisarwat.bookstore.dto.BookForListDTO;
import dev.fadisarwat.bookstore.models.User;
import dev.fadisarwat.bookstore.services.BookRecommendationService;
import dev.fadisarwat.bookstore.services.BookService;
import dev.fadisarwat.bookstore.services.ImageService;
import dev.fadisarwat.bookstore.services.ReviewService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final ImageService imageService;
    private final ReviewService reviewService;
    private final BookRecommendationService bookRecommendationService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor editor = new StringTrimmerEditor(true);

        binder.registerCustomEditor(String.class, editor);
    }

    public BookController(BookService bookService, ImageService imageService, ReviewService reviewService, BookRecommendationService bookRecommendationService) {
        this.bookService = bookService;
        this.imageService = imageService;
        this.reviewService = reviewService;
        this.bookRecommendationService = bookRecommendationService;
    }

    private Integer parseInteger(String value, Integer defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private void sleep(TimeUnit unit, int time) {
        try {
            unit.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @GetMapping
    public Map<String, Object> index(@RequestParam Map<String, String> allParams) {
        int size = parseInteger(allParams.get("size"), 12);
        if (size > 100) size = 100;

        int page = parseInteger(allParams.get("page"), 1);

        Map<String, String> filterParams = allParams.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith("filters["))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        List<Filter> filters = new ArrayList<>();

        for (Map.Entry<String, String> entry : filterParams.entrySet()) {
            String key = entry.getKey().substring(8, entry.getKey().length() - 1);
            String value = entry.getValue();
            filters.add(Filter.of(key, value));
        }

        if(allParams.containsKey("search")) {
            filters.add(
                    new Filter(
                            "title",
                            allParams.get("search"),
                            Filter.Type.FUZZY,
                            new Filter(
                                    "overview",
                                    allParams.get("search"),
                                    Filter.Type.FULL_TEXT
                            )
                    )
            );
        }

        Sort sort = new Sort("id", Sort.Direction.DESC);

        if (allParams.containsKey("sort")) {
            String sortParam = allParams.get("sort");
            sort.setDirection(sortParam.startsWith("-") ? Sort.Direction.DESC : Sort.Direction.ASC);
            sort.setField(sortParam.replaceFirst("[-+]", ""));
        }

        List<String> genres = bookService.allGenres();
        List<String> authors = bookService.allAuthors();
        Pagination<BookForListDTO> books = this.bookService.getBooks(filters, sort, page, size);
        List<BookForListDTO> recommendedBooks = bookRecommendationService.recommendedBooks(User.getCurrentUser());

        return Map.of(
                "books", books,
                "genres", genres,
                "authors", authors,
                "recommendedBooks", recommendedBooks
        );
    }

    @GetMapping("/images/{image}")
    public byte[] getCover(@PathVariable String image, HttpServletResponse response) throws IOException {
        String imageDirectory = Book.coverPath();

        response.setHeader("Cache-Control", "public, max-age=86400");

        if (image.equals("default")) {
            return imageService.getImage("src/main/resources", "book_default.png");
        }

        return imageService.getImage(imageDirectory, image);
    }

    @GetMapping("/{id}")
    public Book show(@PathVariable Long id) {
        // TODO: Remove after testing
//        sleep(TimeUnit.SECONDS, 3);
        User user = User.getCurrentUser();
        Book book = this.bookService.getBook(id);

        if (user != null) {
            Boolean purchased = bookService.isPurchasedByUser(id, user.getId());
            book.setPurchased(purchased);

            if(purchased) {
                book.setWroteReview(
                        book.getReviews()
                                .stream()
                                .anyMatch((review) -> Objects.equals(review.user().getId(), user.getId()))
                );
            }
        }

        bookRecommendationService.saveBrowseHistory(book, user);

        return book;
    }
}
