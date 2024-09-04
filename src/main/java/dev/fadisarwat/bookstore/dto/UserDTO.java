package dev.fadisarwat.bookstore.dto;

import dev.fadisarwat.bookstore.models.User;

import java.util.Date;

public record UserDTO(String firstName, String lastName, String email, String picture, Long timestamp) {
    public static UserDTO fromUser(User user) {
        return new UserDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPicture(),
                new Date().getTime()
        );
    }
}
