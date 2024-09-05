package dev.fadisarwat.bookstore.dao;

import dev.fadisarwat.bookstore.models.Order;

import java.util.List;

public interface OrderDAO {
    List<Order> getOrders();
    List<Order> getOrders(Long userId);
    void saveOrder(Order book);
    Order getOrder(Long id);
    void deleteOrder(Long id);
}
