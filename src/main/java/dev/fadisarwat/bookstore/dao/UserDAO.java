package dev.fadisarwat.bookstore.dao;

import dev.fadisarwat.bookstore.models.User;
import java.util.List;

public interface UserDAO {
    List<User> getUsers();
    User saveUser(User user);
    User getUser(Long id);
    User getUser(String email);
    void deleteUser(Long id);
    void emptyCart(User user);
    Integer cartItemsCount(User user);
}
