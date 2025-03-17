package com.example.vlogi.controllers;

import com.example.vlogi.entity.Article;
import com.example.vlogi.entity.Tag;
import com.example.vlogi.entity.User;
import com.example.vlogi.services.ArticleService;
import com.example.vlogi.services.UserService;
import com.example.vlogi.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;
    private final UserService userService;
    private final TagService tagService;

    @Autowired
    public ArticleController(ArticleService articleService,
                             UserService userService,
                             TagService tagService) {
        this.articleService = articleService;
        this.userService = userService;
        this.tagService = tagService;
    }

    // GET /articles — отображение списка статей (браузерный запрос)
    @GetMapping
    public String showArticles(Model model) {
        List<Article> articles = articleService.findAll();
        model.addAttribute("articles", articles);
        return "articles/list";
    }

    // GET /articles/add — отображение формы для добавления новой статьи
    @GetMapping("/add")
    public String showAddArticleForm(Model model) {
        model.addAttribute("article", new Article());
        // Передаём список авторов и тегов
        model.addAttribute("authors", userService.findAll());
        model.addAttribute("tags", tagService.findAll());
        return "articles/add";
    }

    /**
     * POST /articles
     * Обработка отправки формы для добавления новой статьи
     */
    @PostMapping
    public String createArticle(@RequestParam("title") String title,
                                @RequestParam("description") String description,
                                @RequestParam("body") String body,
                                @RequestParam("authorId") Long authorId,       // <-- Long вместо Integer
                                @RequestParam("tagIds") List<Long> tagIds       // <-- И тут Long
    ) {
        Article article = new Article();
        article.setTitle(title);
        article.setDescription(description);
        article.setContent(body);

        // Получаем автора по Long authorId
        User author = userService.findById(authorId);
        if (author == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid author");
        }
        article.setAuthor(author);

        // Если у статьи есть связь с тегами (ManyToMany),
        // получаем теги по List<Long> tagIds:
        List<Tag> foundTags = tagService.findByIds(tagIds);
        article.setTags(new HashSet<>(foundTags));

        articleService.createArticle(article);
        return "redirect:/articles";
    }

    // GET /articles/details/{id} — отображение деталей статьи
    @GetMapping("/details/{id}")
    public String showArticleDetails(@PathVariable Long id, Model model) { // <-- Long вместо Integer
        Article article = articleService.findById(id);
        model.addAttribute("article", article);
        return "articles/details";
    }
}
