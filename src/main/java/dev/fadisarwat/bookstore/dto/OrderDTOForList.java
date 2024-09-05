package dev.fadisarwat.bookstore.dto;

import dev.fadisarwat.bookstore.models.Order;

import java.util.Date;

public record OrderDTOForList(Long id,
                            Long priceInPennies,
                              String status,
                              Date createdAt,
                              Boolean isPaid,
                              String number) {

    public static OrderDTOForList fromOrder(Order order) {
        return new OrderDTOForList(
                order.getId(),
                order.getPriceInPennies(),
                order.getStatus().getDescription(),
                order.getCreatedAt(),
                order.getPaid(),
                order.getNumber()
        );
    }
}
