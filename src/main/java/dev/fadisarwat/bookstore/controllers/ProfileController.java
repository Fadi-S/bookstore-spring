package dev.fadisarwat.bookstore.controllers;

import dev.fadisarwat.bookstore.exceptions.EmailAlreadyExistsException;
import dev.fadisarwat.bookstore.json.JsonResponse;
import dev.fadisarwat.bookstore.models.User;
import dev.fadisarwat.bookstore.services.ImageService;
import dev.fadisarwat.bookstore.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/profile")
public class ProfileController {

    private final ImageService imageService;
    private final UserService userService;

    ProfileController(ImageService imageService, UserService userService) {
        this.imageService = imageService;
        this.userService = userService;
    }

    @GetMapping("/picture/{picture}")
    public byte[] getPicture(HttpServletResponse response, @PathVariable String picture) throws IOException {
        String imageDirectory = User.picturePath();

        response.setHeader("Cache-Control", "public, max-age=86400");

        if (picture.equals("default")) {
            return imageService.getImage("src/main/resources", "user_default.png");
        }

        return imageService.getImage(imageDirectory, picture);
    }

    @PutMapping("/update/picture")
    public Map<String, Object> updatePicture(@RequestParam("picture") MultipartFile picture) throws IOException {
        String dir = User.picturePath();

        String picturePath = imageService.saveImageToStorage(dir, picture);
        User user = User.getCurrentUser();
        String oldPicture = user.getPicture(true);
        if(oldPicture != null) {
            imageService.deleteImage(dir, oldPicture);
        }

        user.setPicture(picturePath);
        userService.saveUser(user);

        return user.get();
    }

    public record UserProfile(String firstName, String lastName, String email) { }
    @PatchMapping(path="/update")
    public Map<String, Object> updateProfile(@RequestBody UserProfile userProfile) {
        User currentUser = User.getCurrentUser();

        User userWithNewEmail = userService.getUser(userProfile.email());
        if(userWithNewEmail != null && !Objects.equals(userWithNewEmail.getId(), currentUser.getId())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        currentUser.updateUsing(userProfile);

        userService.saveUser(currentUser);

        return currentUser.get();
    }

    @PatchMapping("/update/password")
    public ResponseEntity<JsonResponse> updatePassword(@RequestBody Map<String, String> passwords) {
        String old = passwords.get("currentPassword");
        String newPassword = passwords.get("newPassword");
        String newPasswordConf = passwords.get("confirmPassword");

        JsonResponse response = new JsonResponse();

        User user = User.getCurrentUser();
        JSONObject errors = new JSONObject();

        if (! user.matchPassword(old)) {
            errors.put("currentPassword", "Incorrect password");
        }

        if(!Objects.equals(newPassword, newPasswordConf)) {
            errors.put("confirmPassword", "Passwords do not match");
        }

        if (newPassword.length() < 6) {
            errors.put("newPassword", "Password must be bigger than 6 characters");
        }

        if(! errors.isEmpty()) {
            response.setErrors(errors);
            response.setHttpStatus(HttpStatus.BAD_REQUEST);
        } else {
            response.setMessage("Password changed successfully");
        }

        user.setPassword(newPassword);

        userService.saveUser(user);

        return response.get();
    }

}
