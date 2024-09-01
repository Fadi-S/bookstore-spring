package dev.fadisarwat.bookstore.services;

import dev.fadisarwat.bookstore.dao.OrderDAO;
import dev.fadisarwat.bookstore.models.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDAO orderDOA;

    public OrderServiceImpl(OrderDAO orderDOA) {
        this.orderDOA = orderDOA;
    }

    @Override
    @Transactional
    public void saveOrder(Order order) {
        orderDOA.saveOrder(order);
    }

    @Override
    @Transactional
    public Order getOrder(Long id) {
        return orderDOA.getOrder(id);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        orderDOA.deleteOrder(id);
    }

    @Override
    @Transactional
    public List<Order> getOrders() {
        return orderDOA.getOrders();
    }
}
