package dev.fadisarwat.bookstore.controllers;


import dev.fadisarwat.bookstore.dto.ShoppingCartItemDTO;
import dev.fadisarwat.bookstore.models.Book;
import dev.fadisarwat.bookstore.models.ShoppingCartItem;
import dev.fadisarwat.bookstore.models.User;
import dev.fadisarwat.bookstore.services.BookService;
import dev.fadisarwat.bookstore.services.UserService;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CartController {

    private UserService userService;
    private BookService bookService;
    CartController(UserService userService, BookService bookService) {
        this.userService = userService;
        this.bookService = bookService;
    }

    @GetMapping("/cart")
    public Map<String, Object> index() {
        User user = this.userService.loadUserCart(User.getCurrentUser());

        List<ShoppingCartItemDTO> items = user.getBooksInCart().stream().map(ShoppingCartItemDTO::fromShoppingCartItem).toList();

        return Map.of(
                "items", items,
                "total", items.stream().reduce(0.0, (acc, item) -> acc + item.totalPriceInPennies(), Double::sum)
        );
    }

    @PostMapping("/cart/{bookId}/add")
    public Map<String, Object> addToCart(@PathVariable String bookId) {
        Book book = this.bookService.getBook(Long.parseLong(bookId));
        User user = this.userService.loadUserCart(User.getCurrentUser());

        ShoppingCartItem item = new ShoppingCartItem(book, user, 1L);
        user.addBookToCart(item);
        this.userService.saveUser(user);

        return Map.of("success", true);
    }
}
