package dev.fadisarwat.bookstore.services;

import dev.fadisarwat.bookstore.dao.BookRecommendationDAO;
import dev.fadisarwat.bookstore.dao.BookRecommendationDAOImpl;
import dev.fadisarwat.bookstore.dto.BookForListDTO;
import dev.fadisarwat.bookstore.dto.WeightedBook;
import dev.fadisarwat.bookstore.models.Book;
import dev.fadisarwat.bookstore.models.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookRecommendationServiceImpl implements BookRecommendationService {

    private final BookRecommendationDAO recommendationDAO;

    BookRecommendationServiceImpl(BookRecommendationDAO recommendationDAO) {
        this.recommendationDAO = recommendationDAO;
    }

    @Override
    @Transactional
    public void saveBrowseHistory(Book book, User user) {
        recommendationDAO.saveBrowseHistory(book, user);
    }

    @Override
    @Transactional
    public List<BookForListDTO> recommendedBooks(User user) {
        List<WeightedBook> books = recommendationDAO.browseHistory(user);
        books.addAll(recommendationDAO.purchaseHistory(user));
        System.out.println("books: " + books);
//        List<Long> bookIds = books.stream().map(We::getId).toList();

        // Filter duplicate books
        books = books.stream().distinct().toList();

        return books.subList(0, Math.min(books.size(), 5)).stream().map(
                (wb) -> BookForListDTO.fromBook(wb.book)
        ).toList();
    }
}
