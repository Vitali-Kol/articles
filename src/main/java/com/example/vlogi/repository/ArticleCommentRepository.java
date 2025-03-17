package com.example.vlogi.repository;

import com.example.vlogi.entity.Article;
import com.example.vlogi.entity.ArticleComment;
import com.example.vlogi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Integer> {

    List<ArticleComment> findByArticle(Article article);

    List<ArticleComment> findByUser(User user);
}
