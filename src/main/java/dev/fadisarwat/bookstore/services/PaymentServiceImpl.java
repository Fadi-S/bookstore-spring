package dev.fadisarwat.bookstore.services;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentIntent;
import com.stripe.param.CustomerCreateParams;
import com.stripe.param.PaymentIntentCreateParams;
import dev.fadisarwat.bookstore.models.User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    private Customer getCustomer(User user) {
        if (user == null || user.getStripeId() == null) {
            return null;
        }

        try {
            return Customer.retrieve(user.getStripeId());
        }catch (StripeException e) {
            return null;
        }
    }

    @Override
    public String createCustomer(User user) {
        CustomerCreateParams params = CustomerCreateParams.builder()
                .setName(user.getFullName())
                .setEmail(user.getEmail())
                .setMetadata(Map.of("user_id", user.getId().toString()))
                .build();

        try {
            return Customer.create(params).getId();
        } catch (StripeException e) {
            return null;
        }
    }

    @Override
    public Boolean charge(User user, Long amountInPennies) {
        Customer customer = getCustomer(user);
        if (customer == null) return false;

        String defaultPaymentMethodId = customer.getInvoiceSettings().getDefaultPaymentMethod();

        if (defaultPaymentMethodId == null) return false;

        return charge(user, amountInPennies, defaultPaymentMethodId);
    }

    @Override
    public Boolean charge(User user, Long amountInPennies, String paymentMethodId) {
        if(paymentMethodId == null || paymentMethodId.equalsIgnoreCase("")) return charge(user, amountInPennies);

        Customer customer = getCustomer(user);
        if (customer == null) return false;

        PaymentIntentCreateParams createParams = PaymentIntentCreateParams.builder()
                .setAmount(amountInPennies)
                .setCurrency("usd")
                .setCustomer(customer.getId())
                .setPaymentMethod(paymentMethodId)
                .setOffSession(true)
                .setConfirm(true)
                .build();

        try {
            PaymentIntent.create(createParams);
            return true;
        }catch (StripeException e) {
            return false;
        }
    }
}
