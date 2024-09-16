package dev.fadisarwat.bookstore.services;

import dev.fadisarwat.bookstore.models.User;

public interface PaymentService {
    String createCustomer(User user);
    Boolean charge(User user, Long amountInPennies);
    Boolean charge(User user, Long amountInPennies, String paymentMethodId);
}
