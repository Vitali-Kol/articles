package com.example.vlogi.repository;

import com.example.vlogi.entity.Article;
import com.example.vlogi.entity.ArticleTag;
import com.example.vlogi.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArticleTagRepository extends JpaRepository<ArticleTag, Integer> {

    List<ArticleTag> findByArticle(Article article);

    List<ArticleTag> findByTag(Tag tag);
}
