package com.example.vlogi;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.vlogi.entity.User;
import com.example.vlogi.entity.Roles;
import com.example.vlogi.repository.UserRepository;

@Component
public class UserInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void createUsers() {
        createUserIfNotExists("user1", "user1@example.com", "password1");
        createUserIfNotExists("user2", "user2@example.com", "password2");
        createUserIfNotExists("user3", "user3@example.com", "password3");
    }

    private void createUserIfNotExists(String username, String email, String password) {
        if (!userRepository.existsByUsername(username)) {
            User user = new User();
            user.setUsername(username);
            user.setEmail(email);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(Roles.USER_ROLE); // Устанавливаем роль USER
            userRepository.save(user);
        }
    }
}
