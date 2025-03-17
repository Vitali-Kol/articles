package com.example.vlogi.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(length = 20, unique = true, nullable = false)
    private String name;

    // Конструкторы
    public Tag() {
    }

    public Tag(Integer id, LocalDateTime createdAt, String name) {
        this.id = id;
        this.createdAt = createdAt;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
