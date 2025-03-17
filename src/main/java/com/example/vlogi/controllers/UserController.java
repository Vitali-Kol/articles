package com.example.vlogi.controllers;

import com.example.vlogi.entity.User;
import com.example.vlogi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ========================
    // REST API endpoints (JSON)
    // ========================

    // GET /users/api — вернуть список пользователей в JSON
    @GetMapping("/api")
    @ResponseBody
    public List<User> getAllUsersApi() {
        return userService.findAll();
    }

    // GET /users/api/{id} — вернуть пользователя по ID (JSON)
    @GetMapping("/api/{id}")
    @ResponseBody
    public User getUserByIdApi(@PathVariable Integer id) {
        return userService.findById(id);
    }

    // POST /users/api — создать нового пользователя (JSON)
    @PostMapping("/api")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public User createUserApi(@RequestBody User user) {
        return userService.createUser(user);
    }

    // PUT /users/api/{id} — обновить пользователя (JSON)
    @PutMapping("/api/{id}")
    @ResponseBody
    public User updateUserApi(@PathVariable Integer id, @RequestBody User updatedUser) {
        return userService.updateUser(id, updatedUser);
    }

    // DELETE /users/api/{id} — удалить пользователя (JSON)
    @DeleteMapping("/api/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserApi(@PathVariable Integer id) {
        userService.deleteUser(id);
    }

    // ========================
    // Методы для представлений (Thymeleaf)
    // ========================

    // GET /users — показать всех пользователей (HTML)
    @GetMapping
    public String showUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "users/list"; // шаблон src/main/resources/templates/users/list.html
    }

    /**
     * GET /users/add
     * Отображение формы для добавления нового пользователя
     */
    @GetMapping("/add")
    public String showAddUserForm(Model model) {
        model.addAttribute("user", new User());
        return "users/add"; // шаблон src/main/resources/templates/users/add.html
    }

    /**
     * POST /users
     * Обработка отправки формы для добавления нового пользователя
     */
    @PostMapping
    public String createUser(@ModelAttribute("user") User user) {
        userService.createUser(user);
        return "redirect:/users";
    }
}
