package dev.fadisarwat.bookstore.controllers;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.model.PaymentMethod;
import com.stripe.model.SetupIntent;
import com.stripe.param.CustomerUpdateParams;
import com.stripe.param.SetupIntentCreateParams;
import dev.fadisarwat.bookstore.models.User;
import dev.fadisarwat.bookstore.services.PaymentService;
import dev.fadisarwat.bookstore.services.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final UserService userService;

    public PaymentController(PaymentService paymentService, UserService userService) {
        this.paymentService = paymentService;
        this.userService = userService;
    }

    @PostMapping("/create-setup-intent")
    public Map<String, String> createPaymentIntent() throws StripeException {
        User user = User.getCurrentUser();
        if(user.getStripeId() == null) {
            String customerId = paymentService.createCustomer(user);
            user.setStripeId(customerId);
            userService.saveUser(user);
        }

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
    public Map<String, Object> confirmSetupIntent(@RequestParam("intentId") String intentId) throws Exception {
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

        return Map.of("payment_method_id", intent.getPaymentMethod());
    }

    public record PaymentMethodDTO(String id, String cardBrand, String last4, Long expMonth, Long expYear, String issuer) {
        public static PaymentMethodDTO fromPaymentMethod(PaymentMethod paymentMethod) {
            return new PaymentMethodDTO(
                    paymentMethod.getId(),
                    paymentMethod.getCard().getBrand(),
                    paymentMethod.getCard().getLast4(),
                    paymentMethod.getCard().getExpMonth(),
                    paymentMethod.getCard().getExpYear(),
                    paymentMethod.getCard().getIssuer()
            );
        }
    }

    @GetMapping("/methods")
    public Map<String, Object> getPaymentMethods() throws StripeException {
        User user = User.getCurrentUser();

        Customer customer = Customer.retrieve(user.getStripeId());

        PaymentMethod defaultMethod = customer.getInvoiceSettings().getDefaultPaymentMethodObject();

        return Map.of(
                "list",
                customer.listPaymentMethods().getData().stream().map(PaymentMethodDTO::fromPaymentMethod),
                "default", defaultMethod != null ? defaultMethod.getId() : ""
        );
    }
}
