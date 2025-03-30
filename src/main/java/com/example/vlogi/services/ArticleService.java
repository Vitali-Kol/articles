package com.example.vlogi.services;

import com.example.vlogi.entity.Article;
import com.example.vlogi.entity.Tag;
import com.example.vlogi.entity.User;
import java.util.List;

public interface ArticleService {
    List<Article> findAll();
    Article findById(Long id);
    Article createArticle(Article article);
    Article updateArticle(Long id, Article updatedArticle);
    void deleteArticle(Long id);

    List<Article> findByAuthor(User user);
    List<Article> findByTag(Tag tag);
    List<Article> searchByTitleOrContent(String keyword);


}
