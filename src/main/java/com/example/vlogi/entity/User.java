package com.example.vlogi.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(length = 100, nullable = false)
    private String email;

    @Column(length = 30, nullable = false)
    private String username;

    @Column(length = 200)
    private String imageUrl;

    @Column(length = 200, nullable = false)
    private String password;

    @Column(length = 500)
    private String bio;

    // Конструкторы

    public User() {
    }

    public User(Integer id, LocalDateTime createdAt, String email, String username, String imageUrl, String password, String bio) {
        this.id = id;
        this.createdAt = createdAt;
        this.email = email;
        this.username = username;
        this.imageUrl = imageUrl;
        this.password = password;
        this.bio = bio;
    }

    // Геттеры и сеттеры

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
