package dev.fadisarwat.bookstore.dao;

import dev.fadisarwat.bookstore.models.Order;
import dev.fadisarwat.bookstore.models.User;

import java.util.List;

public interface OrderDAO {
    List<Order> getOrders();
    List<Order> getOrdersPaginated(int page, int size);
    List<Order> getOrders(User user);
    void saveOrder(Order order);
    Order getOrder(Long id);
    void deleteOrder(Long id);
}
