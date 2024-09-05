package dev.fadisarwat.bookstore.controllers;

import dev.fadisarwat.bookstore.dto.OrderDTOForAdmin;
import dev.fadisarwat.bookstore.dto.OrderDTOForList;
import dev.fadisarwat.bookstore.models.Order;
import dev.fadisarwat.bookstore.models.User;
import dev.fadisarwat.bookstore.services.OrderService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;
    AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public List<OrderDTOForList> getOrders(@RequestParam(required = false, defaultValue = "1") int page) {
        return orderService.getOrdersPaginated(page, 10).stream().map(
                OrderDTOForList::fromOrder
        ).toList();
    }

    @GetMapping("/{orderId}")
    public OrderDTOForAdmin getOrder(@PathVariable Long orderId) {
        Order order = orderService.getOrder(orderId);

        if (order == null) {
            throw new NotFoundException("Order not found");
        }

        return OrderDTOForAdmin.fromOrder(order);
    }
}
