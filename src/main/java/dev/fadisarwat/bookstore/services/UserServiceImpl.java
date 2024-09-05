package dev.fadisarwat.bookstore.services;

import dev.fadisarwat.bookstore.dao.UserDAO;
import dev.fadisarwat.bookstore.models.User;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        userDAO.saveUser(user);
    }

    @Override
    @Transactional
    public User getUser(String email) {
        return userDAO.getUser(email);
    }

    @Override
    @Transactional
    public User getUser(Long id) {
        return userDAO.getUser(id);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userDAO.deleteUser(id);
    }

    @Override
    @Transactional
    public List<User> getUsers() {
        return userDAO.getUsers();
    }

    @Override
    @Transactional
    public User loadUserCart(User user) {
        user = userDAO.getUser(user.getId());

        Hibernate.initialize(user.getBooksInCart());

        return user;
    }

    @Override
    @Transactional
    public User loadAddresses(User user) {
        user = userDAO.getUser(user.getId());

        Hibernate.initialize(user.getAddresses());

        return user;
    }

    @Override
    @Transactional
    public User loadAll(User user) {
        user = userDAO.getUser(user.getId());

        Hibernate.initialize(user.getBooksInCart());
        Hibernate.initialize(user.getAddresses());

        return user;
    }

    @Override
    @Transactional
    public void emptyCart(User user) {
        userDAO.emptyCart(user);
    }

    @Override
    public Integer cartItemsCount(User user) {
        return userDAO.cartItemsCount(user);
    }
}
