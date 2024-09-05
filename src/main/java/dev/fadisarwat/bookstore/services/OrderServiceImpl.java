package dev.fadisarwat.bookstore.services;

import dev.fadisarwat.bookstore.dao.BookDAO;
import dev.fadisarwat.bookstore.dao.OrderDAO;
import dev.fadisarwat.bookstore.dao.UserDAO;
import dev.fadisarwat.bookstore.helpers.Pagination;
import dev.fadisarwat.bookstore.models.Address;
import dev.fadisarwat.bookstore.models.Book;
import dev.fadisarwat.bookstore.models.Order;
import dev.fadisarwat.bookstore.models.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderDAO orderDOA;
    private final UserDAO userDAO;
    private final BookDAO bookDAO;

    public OrderServiceImpl(OrderDAO orderDOA, UserDAO userDAO, BookDAO bookDAO) {
        this.orderDOA = orderDOA;
        this.userDAO = userDAO;
        this.bookDAO = bookDAO;
    }

    @Override
    @Transactional
    public void saveOrder(Order order) {
        orderDOA.saveOrder(order);
    }

    @Override
    @Transactional
    public void checkout(User user, Address address) {
        Order order = new Order(
                user,
                address,
                user.cartGeneralTotalInPennies(),
                false,
                Order.Status.PENDING
        );

        List<Book> booksToUpdate = new ArrayList<>();

        user.getBooksInCart().forEach(item -> {
            order.addBookOrder(item.getBook(), item.getQuantity());

            item.getBook().setQuantity(item.getBook().getQuantity() - item.getQuantity());

            booksToUpdate.add(item.getBook());
        });

        bookDAO.saveBooks(booksToUpdate);
        userDAO.emptyCart(user);
        orderDOA.saveOrder(order);
    }

    @Override
    @Transactional
    public Order getOrder(Long id) {
        return orderDOA.getOrder(id);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        orderDOA.deleteOrder(id);
    }

    @Override
    @Transactional
    public List<Order> getOrders() {
        return orderDOA.getOrders();
    }

    @Override
    public Pagination<Order> getOrders(int page, int size)
    {
        return orderDOA.getOrders(page, size);
    }

    @Override
    @Transactional
    public List<Order> getOrders(User user) {
        return orderDOA.getOrders(user);
    }
}
