package dev.fadisarwat.bookstore.dao;

import dev.fadisarwat.bookstore.models.Order;

import java.util.List;

public interface OrderDAO {
    List<Order> getOrders();
    void saveOrder(Order book);
    Order getOrder(Long id);
    void deleteOrder(Long id);
}
