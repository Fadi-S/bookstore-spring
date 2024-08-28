package dev.fadisarwat.bookstore.dao;

import dev.fadisarwat.bookstore.models.User;
import java.util.List;

public interface UserDAO {
    List<User> getUsers();
    void saveUser(User user);
    User getUser(Long id);
    User getUser(String email);
    void deleteUser(Long id);
}
