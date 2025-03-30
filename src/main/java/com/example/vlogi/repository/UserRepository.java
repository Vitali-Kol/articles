package com.example.vlogi.repository;

import com.example.vlogi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface    UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    User findByUsername(String username); // Для поиска пользователя по имени
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    List<User> findByEmailContainingIgnoreCase(String email);

}
