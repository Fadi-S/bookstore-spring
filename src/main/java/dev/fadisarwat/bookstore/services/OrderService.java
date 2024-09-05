package dev.fadisarwat.bookstore.services;

import dev.fadisarwat.bookstore.models.Address;
import dev.fadisarwat.bookstore.models.Order;
import dev.fadisarwat.bookstore.models.User;

import java.util.List;

public interface OrderService {
    List<Order> getOrders();
    List<Order> getOrders(Long userId);
    void saveOrder(Order order);
    Order getOrder(Long id);
    void deleteOrder(Long id);
    void checkout(User user, Address address);
}
