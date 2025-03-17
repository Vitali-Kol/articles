package com.example.vlogi.services;

import com.example.vlogi.entity.User;
import com.example.vlogi.repository.UserRepository;
import com.example.vlogi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    @Override
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
        }
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already in use");
        }
        return userRepository.save(user);
    }

    @Override
    public User updateUser(Integer id, User updatedUser) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (!existing.getEmail().equals(updatedUser.getEmail())) {
            if (userRepository.existsByEmail(updatedUser.getEmail())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
            }
            existing.setEmail(updatedUser.getEmail());
        }
        if (!existing.getUsername().equals(updatedUser.getUsername())) {
            if (userRepository.existsByUsername(updatedUser.getUsername())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already in use");
            }
            existing.setUsername(updatedUser.getUsername());
        }
        existing.setPassword(updatedUser.getPassword());
        existing.setImageUrl(updatedUser.getImageUrl());
        existing.setBio(updatedUser.getBio());
        return userRepository.save(existing);
    }

    @Override
    public void deleteUser(Integer id) {
        User existing = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        userRepository.delete(existing);
    }
}
