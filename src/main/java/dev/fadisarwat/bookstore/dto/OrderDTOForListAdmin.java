package dev.fadisarwat.bookstore.dto;

import dev.fadisarwat.bookstore.models.Order;

import java.util.Date;

public record OrderDTOForListAdmin(Long id,
                                   Long priceInPennies,
                                   String status,
                                   Date createdAt,
                                   Boolean isPaid,
                                   String number,
                                   UserDTO user
) {

    public static OrderDTOForListAdmin fromOrder(Order order) {
        return new OrderDTOForListAdmin(
                order.getId(),
                order.getPriceInPennies(),
                order.getStatus().getDescription(),
                order.getCreatedAt(),
                order.getPaid(),
                order.getNumber(),
                UserDTO.fromUser(order.getUser())
        );
    }
}
