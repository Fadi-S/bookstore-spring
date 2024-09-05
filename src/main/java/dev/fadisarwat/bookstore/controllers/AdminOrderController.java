package dev.fadisarwat.bookstore.controllers;

import dev.fadisarwat.bookstore.dto.OrderDTO;
import dev.fadisarwat.bookstore.dto.OrderDTOForAdmin;
import dev.fadisarwat.bookstore.dto.OrderDTOForList;
import dev.fadisarwat.bookstore.dto.OrderDTOForListAdmin;
import dev.fadisarwat.bookstore.helpers.Pagination;
import dev.fadisarwat.bookstore.models.Order;
import dev.fadisarwat.bookstore.services.OrderService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/orders")
public class AdminOrderController {

    private final OrderService orderService;
    AdminOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Pagination<OrderDTOForListAdmin> getOrders(@RequestParam(required = false, defaultValue = "1") int page, @RequestParam(required = false, defaultValue = "10") int size) {
        return orderService.getOrders(page, size).mapElements(OrderDTOForListAdmin::fromOrder);
    }

    @GetMapping("/{orderId}")
    public OrderDTOForAdmin getOrder(@PathVariable Long orderId) {
        Order order = orderService.getOrder(orderId);

        if (order == null) {
            throw new NotFoundException("Order not found");
        }

        return OrderDTOForAdmin.fromOrder(order);
    }

    @PatchMapping("/{orderId}")
    public OrderDTOForAdmin updateOrderStatus(@PathVariable Long orderId, @RequestBody Map<String, String> body) {
        Order order = orderService.getOrder(orderId);

        if (order == null) {
            throw new NotFoundException("Order not found");
        }

        order.setStatus(Order.Status.valueOf(body.get("status").toUpperCase()));

        orderService.saveOrder(order);

        return OrderDTOForAdmin.fromOrder(order);
    }
}
