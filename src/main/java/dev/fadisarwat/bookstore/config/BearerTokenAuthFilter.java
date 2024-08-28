package dev.fadisarwat.bookstore.config;

import dev.fadisarwat.bookstore.exceptions.AuthenticationFailedException;
import dev.fadisarwat.bookstore.models.OauthToken;
import dev.fadisarwat.bookstore.models.User;
import dev.fadisarwat.bookstore.services.OauthTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class BearerTokenAuthFilter extends OncePerRequestFilter {

    private OauthTokenService oauthTokenService;
    public BearerTokenAuthFilter(OauthTokenService oauthTokenService) {
        this.oauthTokenService = oauthTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ") || authHeader.substring(7).isBlank()) {
            throw new AuthenticationFailedException("Token required");
        }
        String token = authHeader.substring(7);
        User user = getUserFromToken(token);
        if(user == null) {
            throw new AuthenticationFailedException("Invalid token");
        }
        Authentication authenticationToken =
                new UsernamePasswordAuthenticationToken(user.getId(), null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }

    private User getUserFromToken(String token) {
        OauthToken oauthToken = this.oauthTokenService.getToken(token);

        if (oauthToken == null) {
            return null;
        }

        return oauthToken.getUser();
    }

    private void writeResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(message);
    }
}
