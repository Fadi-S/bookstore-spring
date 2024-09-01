package dev.fadisarwat.bookstore.services;

import dev.fadisarwat.bookstore.models.User;

import java.util.List;

public interface UserService {
    List<User> getUsers();
    void saveUser(User user);
    User getUser(Long id);
    User getUser(String email);
    void deleteUser(Long id);
    User loadUserCart(User user);
}
