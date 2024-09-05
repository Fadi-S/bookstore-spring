package dev.fadisarwat.bookstore.config;

import dev.fadisarwat.bookstore.exceptions.ExceptionHandlerFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.sql.DataSource;
import java.util.Collections;

@Configuration
public class SecurityConfig {

    private final BearerTokenAuthFilter bearerTokenAuthFilter;

    SecurityConfig(BearerTokenAuthFilter bearerTokenAuthFilter) {
        this.bearerTokenAuthFilter = bearerTokenAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(request -> {
                    request.requestMatchers("/api/register", "/api/login", "/api/books/**", "/api/profile/picture/**").permitAll();
                    request.requestMatchers("/api/admin/**").hasAuthority("ROLE_ADMIN");
                    request.requestMatchers("/api/**").authenticated();
                })
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterAfter(bearerTokenAuthFilter, BasicAuthenticationFilter.class)
                .addFilterAfter(new ExceptionHandlerFilter(), BasicAuthenticationFilter.class)
        ;
        return http.build();
    }

    @Bean
    public JdbcUserDetailsManager userDetailsService(DataSource dataSource) {
        return new JdbcUserDetailsManager(dataSource);
    }
}
