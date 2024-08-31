package dev.fadisarwat.bookstore.controllers;

import dev.fadisarwat.bookstore.exceptions.AuthenticationFailedException;
import dev.fadisarwat.bookstore.json.JsonResponse;
import dev.fadisarwat.bookstore.models.User;
import dev.fadisarwat.bookstore.services.OauthTokenService;
import dev.fadisarwat.bookstore.services.UserService;
import org.json.JSONObject;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    private OauthTokenService oauthTokenService;
    private UserService userService;

    AuthenticationController(OauthTokenService oauthTokenService, UserService userService) {
        this.oauthTokenService = oauthTokenService;
        this.userService = userService;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        StringTrimmerEditor editor = new StringTrimmerEditor(true);

        binder.registerCustomEditor(String.class, editor);
    }

    @PostMapping("/login")
    public Map<String, Object> login(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String password
    ) {
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
            JsonResponse response = new JsonResponse();
            response.setErrors(new JSONObject().put("email", "User already exists"));

            return response.toMap();
        }

        return new JSONObject()
                .put("token", this.oauthTokenService.createToken(user).getToken())
                .put("user", user.get())
                .put("authorities", user.getAuthoritiesString())
                .toMap();
    }
}
