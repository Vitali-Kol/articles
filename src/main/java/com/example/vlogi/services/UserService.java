package com.example.vlogi.services;

import com.example.vlogi.entity.User;
import java.util.List;

public interface UserService {

    List<User> findAll();
    User findById(Long id);
    User findByUsername(String username); // Добавляем этот метод
    User createUser(User user);
    User updateUser(Long id, User updatedUser);
    void deleteUser(Long id);
}
