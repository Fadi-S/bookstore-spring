package dev.fadisarwat.bookstore.controllers;

import dev.fadisarwat.bookstore.dto.BookForListDTO;
import dev.fadisarwat.bookstore.dto.OrderDTO;
import dev.fadisarwat.bookstore.dto.ShoppingCartItemDTO;
import dev.fadisarwat.bookstore.models.Address;
import dev.fadisarwat.bookstore.models.Book;
import dev.fadisarwat.bookstore.models.Order;
import dev.fadisarwat.bookstore.models.User;
import dev.fadisarwat.bookstore.services.*;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api")
public class CartController {

    private final UserService userService;
    private final BookService bookService;
    private final OrderService orderService;
    private final AddressService addressService;
    private final PaymentService paymentService;
    CartController(UserService userService, BookService bookService, OrderService orderService, AddressService addressService, PaymentService paymentService) {
        this.userService = userService;
        this.bookService = bookService;
        this.orderService = orderService;
        this.addressService = addressService;
        this.paymentService = paymentService;
    }

    @GetMapping("/cart")
    public Map<String, Object> index() {
        User user = this.userService.loadUserCart(User.getCurrentUser());

        List<ShoppingCartItemDTO> items = user.getBooksInCart().stream().map(ShoppingCartItemDTO::fromShoppingCartItem).toList();

        Long subTotal = user.cartTotalPriceInPennies();
        Long tax = Math.round(subTotal * 0.14);
        Long shipping = subTotal != 0 ? 100L : 0;

        return Map.of(
                "items", items,
                "subTotal", subTotal,
                "tax", tax,
                "shipping", shipping,
                "total", subTotal + tax + shipping
        );
    }

    @GetMapping("/cart/count")
    public Integer itemsCount() {
        User user = User.getCurrentUser();
        return userService.cartItemsCount(user);
    }

    @PostMapping("/cart/{bookId}/add")
    public Map<String, Object> addToCart(@PathVariable String bookId, @RequestParam(required = false) Long quantity, HttpServletResponse response) {
        Book book = this.bookService.getBook(Long.parseLong(bookId));
        User user = this.userService.loadUserCart(User.getCurrentUser());

        Boolean success = user.addToCart(book, quantity);
        this.userService.saveUser(user);

        response.setStatus(success ? HttpServletResponse.SC_OK : HttpServletResponse.SC_BAD_REQUEST);

        return Map.of("message", success ? "success" : "Book out of stock");
    }

    @DeleteMapping("/cart/{bookId}/remove")
    public Map<String, Object> removeFromCart(@PathVariable String bookId) {
        Book book = this.bookService.getBook(Long.parseLong(bookId));
        User user = this.userService.loadUserCart(User.getCurrentUser());

        user.removeFromCart(book);
        this.userService.saveUser(user);

        return Map.of("message", "success");
    }

    @PostMapping("/cart/checkout")
    public Object checkout(HttpServletResponse response, @RequestParam String addressId, @RequestParam String paymentMethod) {
        User user = this.userService.loadUserCart(User.getCurrentUser());
        Address address = this.addressService.getAddress(Long.parseLong(addressId));

        if(address == null || !Objects.equals(address.getUser().getId(), user.getId())) {
            throw new NotFoundException("Address not found");
        }

        if (user.getBooksInCart().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return Map.of("message", "Your cart is empty");
        }

        // check if the books are still in stock
        List<BookForListDTO> outOfStockBooks = user.getBooksInCart().stream()
                .filter(item -> item.getBook().getQuantity() < item.getQuantity())
                .map((item) -> BookForListDTO.fromBook(item.getBook()))
                .toList();

        if (!outOfStockBooks.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            Map<String, Object> res = new java.util.HashMap<>(Map.of("message", "We don't have enough stock to fulfill your order"));
            res.put("books", outOfStockBooks);
            return res;
        }

        Order order = this.orderService.checkout(user, address);

        Boolean charged = this.paymentService.charge(user, order.getPriceInPennies(), paymentMethod);
        if (charged) {
            order.setPaid(true);
            this.orderService.saveOrder(order);
        }

        return OrderDTO.fromOrder(order);
    }
}
