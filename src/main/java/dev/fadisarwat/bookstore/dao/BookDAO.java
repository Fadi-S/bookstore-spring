package dev.fadisarwat.bookstore.dao;

import dev.fadisarwat.bookstore.helpers.Filter;
import dev.fadisarwat.bookstore.helpers.Sort;
import dev.fadisarwat.bookstore.models.Book;

import java.util.List;

public interface BookDAO {
    List<Book> getBooks(List<Filter> filters, Sort sort, int page, int size);
    void saveBook(Book book);
    Book getBook(Long id);
    void deleteBook(Long id);
    void saveBooks(List<Book> books);
}
