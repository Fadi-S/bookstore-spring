package dev.fadisarwat.bookstore.dao;

import dev.fadisarwat.bookstore.dto.WeightedBook;
import dev.fadisarwat.bookstore.models.Book;
import dev.fadisarwat.bookstore.models.User;

import java.util.List;

public interface BookRecommendationDAO {
    void saveBrowseHistory(Book book, User user);
    List<WeightedBook> browseHistory(User user);
    List<WeightedBook> purchaseHistory(User user);
}
