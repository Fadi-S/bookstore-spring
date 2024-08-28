package dev.fadisarwat.bookstore.json;

import dev.fadisarwat.bookstore.exceptions.AuthenticationFailedException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<JsonResponse> handleException(Exception e) {
        HttpStatus status = switch (e.getClass().getSimpleName()) {
            case "AuthenticationFailedException" -> HttpStatus.UNAUTHORIZED;
            case "NoResourceFoundException" -> HttpStatus.NOT_FOUND;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        return handleException(status, e);
    }

    private ResponseEntity<JsonResponse> handleException(HttpStatus status, Exception e) {
        JsonResponse response = new JsonResponse();

        response.setStatus(status.value());
        response.setMessage(e.getMessage());

        return new ResponseEntity<>(response, status);

    }
}
