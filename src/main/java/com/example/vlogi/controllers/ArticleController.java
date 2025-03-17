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

    // ========================
    // REST API endpoints (JSON)
    // Префикс "/api" для различения
    // ========================

    // GET /articles/api — вернуть список статей в JSON
    @GetMapping("/api")
    @ResponseBody
    public List<Article> getAllArticlesApi() {
        return articleService.findAll();
    }

    // GET /articles/api/{id} — вернуть статью по ID в JSON
    @GetMapping("/api/{id}")
    @ResponseBody
    public Article getArticleByIdApi(@PathVariable Integer id) {
        return articleService.findById(id);
    }

    // POST /articles/api — создать новую статью (JSON)
    @PostMapping("/api")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Article createArticleApi(@RequestBody Article article) {
        return articleService.createArticle(article);
    }

    // PUT /articles/api/{id} — обновить статью (JSON)
    @PutMapping("/api/{id}")
    @ResponseBody
    public Article updateArticleApi(@PathVariable Integer id, @RequestBody Article updatedArticle) {
        return articleService.updateArticle(id, updatedArticle);
    }

    // DELETE /articles/api/{id} — удалить статью (JSON)
    @DeleteMapping("/api/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteArticleApi(@PathVariable Integer id) {
        articleService.deleteArticle(id);
    }

    // ========================
    // Представления (Thymeleaf-шаблоны)
    // ========================

    // GET /articles — отображение списка статей (браузерный запрос)
    @GetMapping
    public String showArticles(Model model) {
        List<Article> articles = articleService.findAll();
        model.addAttribute("articles", articles);
        return "articles/list"; // шаблон src/main/resources/templates/articles/list.html
    }

    // GET /articles/add — отображение формы для добавления новой статьи
    @GetMapping("/add")
    public String showAddArticleForm(Model model) {
        model.addAttribute("article", new Article());
        // Передаем список авторов и тегов через соответствующие сервисы
        model.addAttribute("authors", userService.findAll());
        model.addAttribute("tags", tagService.findAll()); // Список тегов для выбора
        return "articles/add"; // шаблон src/main/resources/templates/articles/add.html
    }

    /**
     * POST /articles
     * Обработка отправки формы для добавления новой статьи
     */
    @PostMapping
    public String createArticle(@ModelAttribute("article") Article article, @RequestParam("tagIds") List<Integer> tagIds) {
        // Если нет автора или его id, выбрасываем ошибку
        if (article.getAuthor() == null || article.getAuthor().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Author is required");
        }
        // Добавляем выбранные теги к статье
        List<Tag> tags = tagService.findAll().stream()
                .filter(tag -> tagIds.contains(tag.getId()))
                .toList();
        article.setTags(new HashSet<>(tags));

        // Метод createArticle внутри сервиса проверит наличие автора и сохранит статью
        articleService.createArticle(article);
        // Перенаправляем на список статей
        return "redirect:/articles";
    }

    // GET /articles/details/{id} — отображение деталей статьи
    @GetMapping("/details/{id}")
    public String showArticleDetails(@PathVariable Integer id, Model model) {
        Article article = articleService.findById(id);
        model.addAttribute("article", article);
        return "articles/details"; // шаблон src/main/resources/templates/articles/details.html
    }
}
