package dev.fadisarwat.bookstore.controllers;

import dev.fadisarwat.bookstore.dto.OrderDTO;
import dev.fadisarwat.bookstore.models.Order;
import dev.fadisarwat.bookstore.models.User;
import dev.fadisarwat.bookstore.services.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderDTO> getOrders() {
        User user = User.getCurrentUser();
        return orderService.getOrders(user).stream().map(
                OrderDTO::fromOrder
        ).toList();
    }
}
