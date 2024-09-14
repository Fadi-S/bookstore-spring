package dev.fadisarwat.bookstore.controllers;

import com.stripe.exception.StripeException;
import com.stripe.model.Customer;
import com.stripe.param.CustomerCreateParams;
import dev.fadisarwat.bookstore.exceptions.AuthenticationFailedException;
import dev.fadisarwat.bookstore.exceptions.EmailAlreadyExistsException;
import dev.fadisarwat.bookstore.json.JsonResponse;
import dev.fadisarwat.bookstore.models.OauthToken;
import dev.fadisarwat.bookstore.models.User;
import dev.fadisarwat.bookstore.services.OauthTokenService;
import dev.fadisarwat.bookstore.services.PaymentService;
import dev.fadisarwat.bookstore.services.UserService;
import org.json.JSONObject;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    private final OauthTokenService oauthTokenService;
    private final UserService userService;
    private final PaymentService paymentService;

    AuthenticationController(OauthTokenService oauthTokenService, UserService userService, PaymentService paymentService) {
        this.oauthTokenService = oauthTokenService;
        this.userService = userService;
        this.paymentService = paymentService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor editor = new StringTrimmerEditor(true);

        binder.registerCustomEditor(String.class, editor);
    }

    public record UserAuth(String email, String password) {}

    @PostMapping("/login")
    public Map<String, Object> login(
            @RequestBody(required = false) UserAuth userAuth
    ) {
        if (userAuth == null) {
            throw new AuthenticationFailedException("Email and password must be provided");
        }

        String email = userAuth.email, password = userAuth.password;

        User user = this.userService.getUser(email);
        if (user == null || !user.matchPassword(password)) {
            throw new AuthenticationFailedException("Credentials doesn't match any user");
        }

        JsonResponse response = new JsonResponse();
        String token = this.oauthTokenService.createToken(user).getToken();

        JSONObject json = new JSONObject();
        json.put("token", token);
        json.put("authorities", user.getAuthoritiesString());
        json.put("user", user.get());
        response.setMessage(json);

        return json.toMap();
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        if(this.userService.getUser(user.getEmail()) != null) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        user = this.userService.saveUser(user);
        user.setStripeId(paymentService.createCustomer(user));

        OauthToken token = this.oauthTokenService.createToken(user);

        return new JSONObject()
                .put("token", token.getToken())
                .put("user", user.get())
                .put("authorities", user.getAuthoritiesString())
                .toMap();
    }

    @DeleteMapping("/logout")
    public Boolean logout(@RequestHeader(value="Authorization") String authHeader) {
        if (authHeader == null) {
            return false;
        }

        String token = authHeader.substring(7);

        this.oauthTokenService.deleteToken(token);

        return true;
    }

    @GetMapping("/user")
    public Map<String, Object> getUser(@RequestHeader(value="Authorization") String authHeader) throws StripeException {
        JsonResponse response = new JsonResponse();
        String token = authHeader.substring(7);
        User user = User.getCurrentUser();

        JSONObject json = new JSONObject();
        json.put("token", token);
        json.put("authorities", user.getAuthoritiesString());
        json.put("user", user.get());
        response.setMessage(json);

        return json.toMap();
    }
}
