package com.example.articles.controllers;

import com.example.articles.entities.Roles;
import com.example.articles.entities.User;
import com.example.articles.repositories.UserRepository;
import com.example.articles.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;

@Controller
public class RegistrationController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public RegistrationController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/registration")
    public String showRegistrationForm() {
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute User user, Model model) {

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            user.setEmail(user.getUsername() + "@example.com");
        }


        if (userRepository.findByUsername(user.getUsername()) != null) {
            model.addAttribute("usernameError", "Логин уже используется");
            return "registration";
        }


        if (userRepository.findByEmail(user.getEmail()) != null) {
            model.addAttribute("emailError", "Email уже занят");
            return "registration";
        }

        user.setRole(Roles.USER_ROLE);
        user.setCreatedAt(LocalDateTime.now());

        if (user.getImageUrl() == null || user.getImageUrl().trim().isEmpty()) {
            user.setImageUrl("https://static.vecteezy.com/system/resources/previews/009/292/244/non_2x/default-avatar-icon-of-social-media-user-vector.jpg");
        }

        if (user.getBio() == null) {
            user.setBio("");
        }

        userService.createUser(user);
        return "redirect:/login";
    }
}
