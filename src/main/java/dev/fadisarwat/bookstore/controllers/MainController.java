package dev.fadisarwat.bookstore.controllers;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @RequestMapping("/api/hello")
    public String index() {
//        throw new AuthenticationFailedException("Test");
        return "index";
    }

    @RequestMapping("/api/test")
    public String test() {
        return SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
    }
}
