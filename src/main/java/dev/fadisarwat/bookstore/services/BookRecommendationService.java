package dev.fadisarwat.bookstore.services;

import dev.fadisarwat.bookstore.dto.BookForListDTO;
import dev.fadisarwat.bookstore.models.Book;
import dev.fadisarwat.bookstore.models.User;

import java.util.List;

public interface BookRecommendationService {
    void saveBrowseHistory(Book book, User user);
    List<BookForListDTO> recommendedBooks(User user);
}
