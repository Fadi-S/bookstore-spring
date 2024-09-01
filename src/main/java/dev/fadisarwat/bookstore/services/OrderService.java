package dev.fadisarwat.bookstore.services;

import dev.fadisarwat.bookstore.models.Order;

import java.util.List;

public interface OrderService {
    List<Order> getOrders();
    void saveOrder(Order user);
    Order getOrder(Long id);
    void deleteOrder(Long id);
}
