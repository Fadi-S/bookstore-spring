package dev.fadisarwat.bookstore.services;

import dev.fadisarwat.bookstore.helpers.Filter;
import dev.fadisarwat.bookstore.helpers.Sort;
import dev.fadisarwat.bookstore.models.Book;
import dev.fadisarwat.bookstore.dto.BookForListDTO;

import java.util.List;

public interface BookService {
    void saveBook(Book book);
    Book getBook(Long id);
    void deleteBook(Long id);
    void saveBooks(List<Book> books);
    List<BookForListDTO> getBooks(List<Filter> filters, Sort sort, int page, int size);
    List<BookForListDTO> getBooks(List<Filter> filters, Sort sort);
    List<BookForListDTO> getBooks(List<Filter> filters);
    List<BookForListDTO> getBooks(int page, int size);
    List<BookForListDTO> getBooks();
}
