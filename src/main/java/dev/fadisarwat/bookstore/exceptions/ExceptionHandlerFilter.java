package dev.fadisarwat.bookstore.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.fadisarwat.bookstore.json.JsonResponse;
import dev.fadisarwat.bookstore.json.RestExceptionHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AuthenticationFailedException e) {
            RestExceptionHandler handler = new RestExceptionHandler();
            JsonResponse jsonResponse = handler.handleException(e).getBody();

            response.setStatus(jsonResponse.getStatus());
            response.getWriter().write(convertObjectToJson(jsonResponse));
            response.setContentType("application/json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
