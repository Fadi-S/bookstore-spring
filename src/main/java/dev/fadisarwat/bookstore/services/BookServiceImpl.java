package dev.fadisarwat.bookstore.services;

import dev.fadisarwat.bookstore.dao.BookDAO;
import dev.fadisarwat.bookstore.dao.UserDAO;
import dev.fadisarwat.bookstore.models.Book;
import dev.fadisarwat.bookstore.models.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private final BookDAO userDAO;

    public BookServiceImpl(BookDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    @Transactional
    public void saveBook(Book user) {
        userDAO.saveBook(user);
    }

    @Override
    @Transactional
    public Book getBook(Long id) {
        return userDAO.getBook(id);
    }

    @Override
    @Transactional
    public void deleteBook(Long id) {
        userDAO.deleteBook(id);
    }

    @Override
    @Transactional
    public List<Book> getBooks() {
        return userDAO.getBooks();
    }
}
