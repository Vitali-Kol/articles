package com.example.vlogi.repository;

import com.example.vlogi.entity.Article;
import com.example.vlogi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("SELECT a FROM Article a WHERE LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Article> findArticlesByTitleContaining(@Param("keyword") String keyword);

    @Query("SELECT a FROM Article a WHERE a.author = :author")
    List<Article> findArticlesByAuthor(@Param("author") User author);

    @Query("SELECT a FROM Article a JOIN ArticleTag at ON a.id = at.article.id WHERE at.tag.name = :tagName")
    List<Article> findArticlesByTag(@Param("tagName") String tagName);

    @Query("SELECT a FROM Article a WHERE a.createdAt > :date")
    List<Article> findArticlesPublishedAfter(@Param("date") LocalDateTime date);

    @Query("SELECT a FROM Article a ORDER BY (SELECT COUNT(f) FROM ArticleFavorite f WHERE f.article = a) DESC")
    List<Article> findPopularArticles();

    @Query("SELECT a FROM Article a " +
            "WHERE LOWER(a.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "   OR LOWER(a.content) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Article> searchByTitleOrContent(@Param("keyword") String keyword);

    List<Article> findByAuthor(User author);

    boolean existsBySlug(String slug);
}
