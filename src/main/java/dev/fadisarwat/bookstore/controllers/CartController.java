package dev.fadisarwat.bookstore.controllers;

import dev.fadisarwat.bookstore.dto.ShoppingCartItemDTO;
import dev.fadisarwat.bookstore.models.Book;
import dev.fadisarwat.bookstore.models.User;
import dev.fadisarwat.bookstore.services.BookService;
import dev.fadisarwat.bookstore.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
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
    public Map<String, Object> addToCart(@PathVariable String bookId, HttpServletResponse response) {
        Book book = this.bookService.getBook(Long.parseLong(bookId));
        User user = this.userService.loadUserCart(User.getCurrentUser());

        Boolean success = user.addToCart(book);
        this.userService.saveUser(user);

        response.setStatus(success ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);

        return Map.of("message", success ? "success" : "Book out of stock");
    }

    @PostMapping("/cart/{bookId}/remove")
    public Map<String, Object> removeFromCart(@PathVariable String bookId) {
        Book book = this.bookService.getBook(Long.parseLong(bookId));
        User user = this.userService.loadUserCart(User.getCurrentUser());

        user.removeFromCart(book);
        this.userService.saveUser(user);

        return Map.of("message", "success");
    }
}
