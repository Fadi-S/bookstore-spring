package dev.fadisarwat.bookstore.dto;

import dev.fadisarwat.bookstore.models.User;

import java.util.Date;

public record UserForReviewDTO(String firstName, String lastName, String picture) {
    public static UserForReviewDTO fromUser(User user) {
        return new UserForReviewDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getPicture()
        );
    }
}
