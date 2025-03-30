package com.example.vlogi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "article_favorite")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleFavorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // Добавляем поле updatedAt
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Метод для установки времени добавления в избранное
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Метод для установки статьи
    public void setArticle(Article article) {
        this.article = article;
    }

    // Метод для установки пользователя
    public void setUser(User user) {
        this.user = user;
    }

    // Метод для установки времени обновления
    public void setUpdatedAt(LocalDateTime now) {
        this.updatedAt = now;
    }
}
