package dev.fadisarwat.bookstore.controllers;

import com.stripe.Stripe;
import dev.fadisarwat.bookstore.dto.OrderDTO;
import dev.fadisarwat.bookstore.models.User;
import dev.fadisarwat.bookstore.services.OrderService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    OrderController(OrderService orderService, @Value("${stripe.api}") String stripeKey) {
        this.orderService = orderService;
        Stripe.apiKey = stripeKey;
    }

    @GetMapping
    public List<OrderDTO> getOrders() {
        User user = User.getCurrentUser();
        return orderService.getOrders(user).stream().map(
                OrderDTO::fromOrder
        ).toList();
    }
}
