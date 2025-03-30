package com.example.vlogi.controllers;

import com.example.vlogi.entity.User;
import com.example.vlogi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    private final UserService userService;

    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());  // Объект пользователя для формы
        return "register";  // Шаблон для регистрации
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        userService.createUser(user);  // Сохраняем нового пользователя
        return "redirect:/login";  // Переадресовываем на страницу логина
    }
}
