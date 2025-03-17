package com.example.vlogi.services;

import com.example.vlogi.entity.User;
import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(Integer id);
    User createUser(User user);
    User updateUser(Integer id, User user);
    void deleteUser(Integer id);
}
