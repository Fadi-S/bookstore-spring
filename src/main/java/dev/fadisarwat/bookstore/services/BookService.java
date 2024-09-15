package dev.fadisarwat.bookstore.services;

import dev.fadisarwat.bookstore.helpers.Filter;
import dev.fadisarwat.bookstore.helpers.Pagination;
import dev.fadisarwat.bookstore.helpers.Sort;
import dev.fadisarwat.bookstore.models.Book;
import dev.fadisarwat.bookstore.dto.BookForListDTO;
import dev.fadisarwat.bookstore.models.User;

import java.util.List;

public interface BookService {
    Book saveBook(Book book);
    Book getBook(Long id);
    Boolean isPurchasedByUser(Long bookId, Long userId);
    void deleteBook(Long id);
    void saveBooks(List<Book> books);
    List<String> allGenres();
    List<String> allAuthors();
    Pagination<BookForListDTO> getBooks(List<Filter> filters, Sort sort, int page, int size);
    Pagination<BookForListDTO> getBooks(List<Filter> filters, Sort sort);
    Pagination<BookForListDTO> getBooks(List<Filter> filters);
    Pagination<BookForListDTO> getBooks(int page, int size);
    Pagination<BookForListDTO> getBooks();
}
