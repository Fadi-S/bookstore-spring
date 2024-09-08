package dev.fadisarwat.bookstore.services;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import dev.fadisarwat.bookstore.models.User;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Override
    public Boolean charge(User user, Long amountInPennies) {
        if (user == null || user.getStripeId() == null) {
            return false;
        }

        Customer customer;
        try {
            customer = Customer.retrieve(user.getStripeId());
        }catch (StripeException e) {
            return false;
        }

        if (customer == null) {
            return false;
        }

        String defaultPaymentMethodId = customer.getInvoiceSettings().getDefaultPaymentMethod();

        if (defaultPaymentMethodId == null) {
            return false;
        }

        PaymentIntentCreateParams createParams = PaymentIntentCreateParams.builder()
                .setAmount(amountInPennies)
                .setCurrency("usd")
                .setCustomer(customer.getId())
                .setPaymentMethod(defaultPaymentMethodId)
                .setOffSession(true)
                .setConfirm(true)
                .build();

        try {
            PaymentIntent.create(createParams);
        }catch (StripeException e) {
            return false;
        }

        return true;
    }
}
