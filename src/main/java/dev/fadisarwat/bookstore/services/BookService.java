package dev.fadisarwat.bookstore.services;

import dev.fadisarwat.bookstore.models.Book;
import dev.fadisarwat.bookstore.models.User;

import java.util.List;

public interface BookService {
    List<Book> getBooks();
    void saveBook(Book book);
    Book getBook(Long id);
    void deleteBook(Long id);
}
