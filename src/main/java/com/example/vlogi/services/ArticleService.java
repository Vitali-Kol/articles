package com.example.vlogi.services;

import com.example.vlogi.entity.Article;
import java.util.List;

public interface ArticleService {
    List<Article> findAll();
    Article findById(Integer id);
    Article createArticle(Article article);
    Article updateArticle(Integer id, Article article);
    void deleteArticle(Integer id);
}
