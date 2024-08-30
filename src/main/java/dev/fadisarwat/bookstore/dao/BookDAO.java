package dev.fadisarwat.bookstore.dao;

import dev.fadisarwat.bookstore.models.Book;

import java.util.List;

public interface BookDAO {
    List<Book> getBooks();
    void saveBook(Book book);
    Book getBook(Long id);
    void deleteBook(Long id);
}
