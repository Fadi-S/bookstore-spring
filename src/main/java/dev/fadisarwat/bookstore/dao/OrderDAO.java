package dev.fadisarwat.bookstore.dao;

import dev.fadisarwat.bookstore.helpers.Pagination;
import dev.fadisarwat.bookstore.models.Order;
import dev.fadisarwat.bookstore.models.User;

import java.util.List;

public interface OrderDAO {
    List<Order> getOrders();
    Pagination<Order> getOrders(int page, int size);
    List<Order> getOrders(User user);
    Order saveOrder(Order order);
    Order getOrder(Long id);
    void deleteOrder(Long id);
}
