package dev.fadisarwat.bookstore.services;

import dev.fadisarwat.bookstore.dao.BookDAO;
import dev.fadisarwat.bookstore.helpers.Filter;
import dev.fadisarwat.bookstore.helpers.Pagination;
import dev.fadisarwat.bookstore.helpers.Sort;
import dev.fadisarwat.bookstore.models.Book;
import dev.fadisarwat.bookstore.dto.BookForListDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookDAO bookDAO;

    public BookServiceImpl(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @Override
    @Transactional
    public Book saveBook(Book book) {
        return bookDAO.saveBook(book);
    }

    @Override
    @Transactional
    public Boolean isPurchasedByUser(Long bookId, Long userId) {
        return bookDAO.isPurchasedByUser(bookId, userId);
    }

    @Override
    @Transactional
    public Book getBook(Long id) {
        Object[] def = {null, 0};
        Object[] result = bookDAO.getBook(id).orElse(def);
        Book book = (Book) result[0];
        Double averageRating;
        if (result[1] instanceof Integer) {
            Integer res = (Integer) result[1];
            averageRating = Double.valueOf(res);
        }else {
            averageRating = (Double) result[1];
        }
        book.setAverageRating(averageRating);
        return book;
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        bookDAO.deleteBook(id);
    }

    @Override
    @Transactional
    public void saveBooks(List<Book> books) {
        bookDAO.saveBooks(books);
    }

    @Override
    @Transactional
    public Pagination<BookForListDTO> getBooks(List<Filter> filters, Sort sort, int page, int size) {
        return bookDAO.getBooks(filters, sort, page, size).mapElements(BookForListDTO::fromBook);
    }

    @Override
    @Transactional
    public Pagination<BookForListDTO> getBooks(List<Filter> filters, Sort sort) {
        return getBooks(filters, sort, 0, 0);
    }

    @Override
    @Transactional
    public Pagination<BookForListDTO> getBooks(List<Filter> filters) {
        return getBooks(filters, null, 0, 0);
    }

    @Override
    @Transactional
    public Pagination<BookForListDTO> getBooks(int page, int size) {
        return getBooks(null, null, page, size);
    }

    @Override
    @Transactional
    public Pagination<BookForListDTO> getBooks() {
        return getBooks(null, null, 0, 0);
    }
}
