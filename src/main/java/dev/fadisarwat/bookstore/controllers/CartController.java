package dev.fadisarwat.bookstore.controllers;


import dev.fadisarwat.bookstore.dto.BookForListDTO;
import dev.fadisarwat.bookstore.dto.ShoppingCartItemDTO;
import dev.fadisarwat.bookstore.models.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CartController {

    @GetMapping("/cart")
    public Map<String, Object> index() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<ShoppingCartItemDTO> items = user.getBooksInCart().stream().map(ShoppingCartItemDTO::fromShoppingCartItem).toList();

        Map<String, Object> response = Map.of(
                "items", items,
                "total", items.stream().reduce(0.0, (acc, item) -> acc + item.totalPriceInPennies(), Double::sum)
        );

        return response;
    }
}
