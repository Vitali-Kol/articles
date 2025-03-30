package com.example.vlogi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "article_comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArticleComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Поле для времени создания
    @CreationTimestamp
    private LocalDateTime createdAt;

    // Поле для времени обновления
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 500, nullable = false)
    private String content;

    // Метод для установки времени создания
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // Метод для установки времени обновления
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // Метод для установки содержания комментария
    public void setBody(String content) {
        this.content = content;
    }

    // Метод для установки статьи
    public void setArticle(Article article) {
        this.article = article;
    }

    // Метод для установки пользователя
    public void setUser(User user) {
        this.user = user;
    }
}
