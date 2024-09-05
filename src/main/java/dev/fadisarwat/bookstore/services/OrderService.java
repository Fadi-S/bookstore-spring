package dev.fadisarwat.bookstore.services;

import dev.fadisarwat.bookstore.helpers.Pagination;
import dev.fadisarwat.bookstore.models.Address;
import dev.fadisarwat.bookstore.models.Order;
import dev.fadisarwat.bookstore.models.User;

import java.util.List;

public interface OrderService {
    List<Order> getOrders();
    Pagination<Order> getOrders(int page, int size);
    List<Order> getOrders(User user);
    void saveOrder(Order order);
    Order getOrder(Long id);
    void deleteOrder(Long id);
    void checkout(User user, Address address);
}
