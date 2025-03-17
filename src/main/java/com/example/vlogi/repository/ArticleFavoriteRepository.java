package com.example.vlogi.repository;

import com.example.vlogi.entity.Article;
import com.example.vlogi.entity.ArticleFavorite;
import com.example.vlogi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleFavoriteRepository extends JpaRepository<ArticleFavorite, Integer> {

    List<ArticleFavorite> findByUser(User user);

    List<ArticleFavorite> findByArticle(Article article);
}
