package dev.fadisarwat.bookstore.controllers;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.SetupIntent;
import com.stripe.param.CustomerUpdateParams;
import com.stripe.param.SetupIntentCreateParams;
import dev.fadisarwat.bookstore.models.User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    @PostMapping("/create-setup-intent")
    public Map<String, String> createPaymentIntent() throws StripeException {
        User user = User.getCurrentUser();

        SetupIntent intent = SetupIntent.create(
                SetupIntentCreateParams.builder()
                        .setCustomer(user.getStripeId())
                        .setAutomaticPaymentMethods(SetupIntentCreateParams.AutomaticPaymentMethods.builder().setEnabled(true).build())
                        .build()
        );
        Map<String, String> map = new HashMap<>();
        map.put("id", intent.getId());
        map.put("client_secret", intent.getClientSecret());
        return map;
    }

    @PostMapping("/finish-setup-intent")
    public Map<String, String> confirmSetupIntent(@RequestParam("intentId") String intentId) throws Exception {
        User user = User.getCurrentUser();
        SetupIntent intent = SetupIntent.retrieve(intentId);

        if(intent.getPaymentMethod() == null) {
            throw new Exception("Payment method not attached to SetupIntent");
        }

        Customer customer = Customer.retrieve(user.getStripeId());

        customer.update(CustomerUpdateParams.builder().setInvoiceSettings(
                CustomerUpdateParams.InvoiceSettings.builder()
                        .setDefaultPaymentMethod(intent.getPaymentMethod())
                        .build()
        ).build());

        return Map.of("status", "succeeded");
    }
}
