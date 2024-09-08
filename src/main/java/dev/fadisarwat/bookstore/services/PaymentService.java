package dev.fadisarwat.bookstore.services;

import dev.fadisarwat.bookstore.models.User;

public interface PaymentService {
    Boolean charge(User user, Long amountInPennies);
}
