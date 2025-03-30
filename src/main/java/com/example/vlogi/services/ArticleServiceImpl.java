package com.example.vlogi.services;

import com.example.vlogi.entity.Article;
import com.example.vlogi.entity.Tag;
import com.example.vlogi.entity.User;
import com.example.vlogi.repository.ArticleRepository;
import com.example.vlogi.repository.ArticleFavoriteRepository;
import com.example.vlogi.repository.ArticleCommentRepository;
import com.example.vlogi.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleFavoriteRepository articleFavoriteRepository;
    private final ArticleCommentRepository articleCommentRepository;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository,
                              UserRepository userRepository,
                              ArticleFavoriteRepository articleFavoriteRepository,
                              ArticleCommentRepository articleCommentRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
        this.articleFavoriteRepository = articleFavoriteRepository;
        this.articleCommentRepository = articleCommentRepository;
    }

    @Override
    public List<Article> findAll() {
        return articleRepository.findAll();
    }

    @Override
    public Article findById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found"));
    }

    /**
     * Создание статьи.
     * Доступ разрешён только для пользователей с ролью USER, если автор статьи совпадает с текущим пользователем.
     */
    @Override
    @PreAuthorize("hasRole('ROLE_USER') and (#article.author.username == authentication.name)")
    public Article createArticle(Article article) {
        if (article.getAuthor() == null || article.getAuthor().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Author is required");
        }
        // Проверяем наличие автора
        User author = userRepository.findById(article.getAuthor().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid author ID"));
        article.setAuthor(author);
        return articleRepository.save(article);
    }

    @Override
    public Article updateArticle(Long id, Article updatedArticle) {
        Article existing = articleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found"));

        existing.setTitle(updatedArticle.getTitle());
        existing.setDescription(updatedArticle.getDescription());
        existing.setContent(updatedArticle.getContent());
        existing.setTags(updatedArticle.getTags());

        if (updatedArticle.getAuthor() != null && updatedArticle.getAuthor().getId() != null) {
            User newAuthor = userRepository.findById(updatedArticle.getAuthor().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid author ID"));
            existing.setAuthor(newAuthor);
        }

        return articleRepository.save(existing);
    }

    @Override
    @Transactional
    public void deleteArticle(Long id) {
        Article existing = articleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found"));

        articleCommentRepository.deleteByArticleId(id);
        articleFavoriteRepository.deleteByArticleId(id);

        existing.getTags().clear();
        articleRepository.save(existing);
        articleRepository.delete(existing);
    }

    @Override
    public List<Article> findByAuthor(User user) {
        return articleRepository.findByAuthor(user);
    }

    @Override
    public List<Article> findByTag(Tag tag) {
        return articleRepository.findArticlesByTag(tag.getName());
    }

    @Override
    public List<Article> searchByTitleOrContent(String keyword) {
        return articleRepository.searchByTitleOrContent(keyword);
    }
}
