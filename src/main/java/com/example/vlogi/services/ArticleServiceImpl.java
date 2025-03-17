package com.example.vlogi.services;

import com.example.vlogi.entity.Article;
import com.example.vlogi.entity.User;
import com.example.vlogi.repository.ArticleRepository;
import com.example.vlogi.repository.UserRepository;
import com.example.vlogi.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
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

    @Override
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

        // Если передан новый автор, проверяем его наличие
        if (updatedArticle.getAuthor() != null && updatedArticle.getAuthor().getId() != null) {
            User newAuthor = userRepository.findById(updatedArticle.getAuthor().getId())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid author ID"));
            existing.setAuthor(newAuthor);
        }

        return articleRepository.save(existing);
    }

    @Override
    public void deleteArticle(Long id) {
        Article existing = articleRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found"));
        articleRepository.delete(existing);
    }
}
