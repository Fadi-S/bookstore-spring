package dev.fadisarwat.bookstore.controllers;

import dev.fadisarwat.bookstore.dto.BookForListDTO;
import dev.fadisarwat.bookstore.dto.ShoppingCartItemDTO;
import dev.fadisarwat.bookstore.models.Address;
import dev.fadisarwat.bookstore.models.Book;
import dev.fadisarwat.bookstore.models.Order;
import dev.fadisarwat.bookstore.models.User;
import dev.fadisarwat.bookstore.services.BookService;
import dev.fadisarwat.bookstore.services.OrderService;
import dev.fadisarwat.bookstore.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CartController {

    private final UserService userService;
    private final BookService bookService;
    private final OrderService orderService;
    CartController(UserService userService, BookService bookService, OrderService orderService) {
        this.userService = userService;
        this.bookService = bookService;
        this.orderService = orderService;
    }

    @GetMapping("/cart")
    public Map<String, Object> index() {
        User user = this.userService.loadUserCart(User.getCurrentUser());

        List<ShoppingCartItemDTO> items = user.getBooksInCart().stream().map(ShoppingCartItemDTO::fromShoppingCartItem).toList();

        return Map.of(
                "items", items,
                "total", totalPriceInPennies(items)
        );
    }

    private Long totalPriceInPennies(List<ShoppingCartItemDTO> items) {
        return items.stream().reduce(0L, (acc, item) -> acc + item.totalPriceInPennies(), Long::sum);
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

    @PostMapping("/cart/checkout")
    public Map<String, Object> checkout(HttpServletResponse response, @RequestParam String addressId) {
        User user = this.userService.loadAll(User.getCurrentUser());

        Address address = user.getAddresses().stream()
                .filter(a -> a.getId().equals(Long.parseLong(addressId)))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Address not found " + addressId));

        if (user.getBooksInCart().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return Map.of("message", "Cart is empty");
        }

        // check if the books are still in stock
        List<BookForListDTO> outOfStockBooks = user.getBooksInCart().stream()
                .filter(item -> item.getBook().getQuantity() < item.getQuantity())
                .map((item) -> BookForListDTO.fromBook(item.getBook()))
                .toList();

        if (!outOfStockBooks.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            Map<String, Object> res = new java.util.HashMap<>(Map.of("message", "We are out of stock for the following books"));
            res.put("books", outOfStockBooks);
            return res;
        }

        Long price = totalPriceInPennies(user.getBooksInCart().stream().map(ShoppingCartItemDTO::fromShoppingCartItem).toList());
        Order order = new Order(user, address, price, false, Order.Status.PENDING);

        user.getBooksInCart().forEach(item -> {
            order.addBookOrder(item.getBook(), item.getQuantity());

            item.getBook().setQuantity(item.getBook().getQuantity() - item.getQuantity());
        });

        this.orderService.saveOrder(order);

        this.userService.saveUser(user);
        user.setBooksInCart(List.of());
        this.userService.saveUser(user);

        return Map.of("message", "success");
    }
}
