package dev.fadisarwat.bookstore.services;

import dev.fadisarwat.bookstore.dao.BookDAO;
import dev.fadisarwat.bookstore.helpers.Filter;
import dev.fadisarwat.bookstore.helpers.Sort;
import dev.fadisarwat.bookstore.models.Book;
import dev.fadisarwat.bookstore.models.BookForListDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookDAO bookDAO;

    public BookServiceImpl(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    @Override
    @Transactional
    public void saveBook(Book user) {
        bookDAO.saveBook(user);
    }

    @Override
    @Transactional
    public Book getBook(Long id) {
        return bookDAO.getBook(id);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        bookDAO.deleteBook(id);
    }

    @Override
    @Transactional
    public List<BookForListDTO> getBooks(List<Filter> filters, Sort sort, int page, int size) {
        return bookDAO.getBooks(filters, sort, page, size)
                .stream()
                .map(BookForListDTO::fromBook)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<BookForListDTO> getBooks(List<Filter> filters, Sort sort) {
        return getBooks(filters, sort, 0, 0);
    }

    @Override
    @Transactional
    public List<BookForListDTO> getBooks(List<Filter> filters) {
        return getBooks(filters, null, 0, 0);
    }

    @Override
    @Transactional
    public List<BookForListDTO> getBooks(int page, int size) {
        return getBooks(null, null, page, size);
    }

    @Override
    @Transactional
    public List<BookForListDTO> getBooks() {
        return getBooks(null, null, 0, 0);
    }
}
