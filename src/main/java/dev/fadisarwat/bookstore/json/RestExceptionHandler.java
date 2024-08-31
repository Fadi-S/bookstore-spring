package dev.fadisarwat.bookstore.json;

import dev.fadisarwat.bookstore.exceptions.AuthenticationFailedException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.json.JSONObject;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.Arrays;

@ControllerAdvice
public class RestExceptionHandler {

    private final Boolean debug = false;

    @ExceptionHandler
    public ResponseEntity<JsonResponse> handleException(AuthenticationFailedException e) {
        return defaultResponse(HttpStatus.UNAUTHORIZED, e);
    }


    @ExceptionHandler
    public ResponseEntity<JsonResponse> handleException(IllegalArgumentException e) {
        return defaultResponse(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler
    public ResponseEntity<JsonResponse> handleException(NoResourceFoundException e) {
        return defaultResponse(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler
    public ResponseEntity<JsonResponse> handleException(NotFoundException e) {
        return defaultResponse(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler
    public ResponseEntity<JsonResponse> handleException(EmptyResultDataAccessException e) {
        return defaultResponse(HttpStatus.NOT_FOUND, e);
    }

    @ExceptionHandler
    public ResponseEntity<JsonResponse> handleException(MissingServletRequestParameterException e) {
        return defaultResponse(HttpStatus.BAD_REQUEST, e);
    }

    @ExceptionHandler
    public ResponseEntity<JsonResponse> handleException(ConstraintViolationException e) {
        JsonResponse response = new JsonResponse();

        HttpStatus status = HttpStatus.BAD_REQUEST;

        response.setStatus(status.value());
        JSONObject json = new JSONObject();

        JSONObject errors = new JSONObject();

        for (var violation : e.getConstraintViolations()) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        json.put("errors", errors);

        response.setMessage(json);

        return new ResponseEntity<>(response, status);
    }

    private ResponseEntity<JsonResponse> defaultResponse(HttpStatus status, Exception e) {
        JsonResponse response = new JsonResponse();

        response.setStatus(status.value());
        response.setMessage(e.getMessage() + (debug ? " - " + e.getClass().getSimpleName() + Arrays.toString(e.getStackTrace()) : ""));

        return new ResponseEntity<>(response, status);
    }
}
