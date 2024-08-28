package dev.fadisarwat.bookstore.controllers;

import dev.fadisarwat.bookstore.json.JsonResponse;
import dev.fadisarwat.bookstore.models.OauthToken;
import dev.fadisarwat.bookstore.models.User;
import dev.fadisarwat.bookstore.services.OauthTokenService;
import dev.fadisarwat.bookstore.services.UserService;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<JsonResponse> login(@RequestParam String email, @RequestParam String password) {
        User user = this.userService.getUser(email);
        if (user == null || !user.matchPassword(password)) {
            throw new NotFoundException("Credentials doesn't match any user");
        }

        JsonResponse response = new JsonResponse();
        String token = this.oauthTokenService.createToken(user).getToken();

        response.setMessage(token);

        return response.get();
    }

    @PostMapping("/register")
    public ResponseEntity<JsonResponse> register(
            @RequestParam(name="first_name") String firstName,
            @RequestParam(name="last_name") String lastName,
            @RequestParam String email,
            @RequestParam String password
            ) {

        JsonResponse response = new JsonResponse();

        User user = new User(firstName, lastName, email, password);
        OauthToken token = this.oauthTokenService.createToken(user);

        response.setMessage(token.getToken());

        return response.get();
    }
}
