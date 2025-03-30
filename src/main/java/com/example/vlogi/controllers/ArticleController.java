package com.example.vlogi.controllers;

import com.example.vlogi.entity.Article;
import com.example.vlogi.entity.Tag;
import com.example.vlogi.entity.User;
import com.example.vlogi.services.ArticleService;
import com.example.vlogi.services.TagService;
import com.example.vlogi.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
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

    /**
     * GET /articles
     * Показать список всех статей текущего пользователя
     */
    @GetMapping
    public String showArticles(Model model) {
        List<Article> articles = articleService.findAll();
        model.addAttribute("articles", articles);
        return "articles/list";
    }

    /**
     * GET /articles/{id}
     * Показать статью по ID (Детали статьи)
     */
    @GetMapping("/{id}")
    public String showArticleDetails(@PathVariable Long id, Model model) {
        Article article = articleService.findById(id);
        model.addAttribute("article", article);
        return "articles/details";
    }

    /**
     * GET /articles/add
     * Отображение формы для добавления новой статьи
     */
    @GetMapping("/add")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String showAddArticleForm(Model model) {
        // Если автор определяется автоматически, можно не передавать список авторов
        model.addAttribute("article", new Article());
        model.addAttribute("tags", tagService.findAll());
        return "articles/add";
    }

    /**
     * POST /articles
     * Создание новой статьи.
     * Здесь проверяется, что создаёт статью пользователь с ролью USER.
     */
    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER') and (principal.name == #authorName)")
    public String createArticle(@RequestParam("title") String title,
                                @RequestParam("description") String description,
                                @RequestParam("body") String body,
                                @RequestParam("tagIds") List<Long> tagIds,
                                Principal principal) {
        // Здесь автор определяется по имени из principal
        String authorName = principal.getName();
        User author = userService.findByUsername(principal.getName());
        if (author == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid author");
        }

        Article article = new Article();
        article.setTitle(title);
        article.setDescription(description);
        article.setContent(body);
        article.setAuthor(author);

        List<Tag> foundTags = tagService.findByIds(tagIds);
        article.setTags(new HashSet<>(foundTags));

        articleService.createArticle(article);
        return "redirect:/articles";
    }

    /**
     * GET /articles/edit/{id}
     * Отображение формы для редактирования статьи.
     * Доступ разрешён только автору статьи.
     */
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_USER') and (@articleService.findById(#id).author.username == principal.name)")
    public String showEditArticleForm(@PathVariable Long id, Model model, Principal principal) {
        Article article = articleService.findById(id);
        model.addAttribute("article", article);
        model.addAttribute("tags", tagService.findAll());
        return "articles/edit";
    }

    /**
     * POST /articles/edit/{id}
     * Обновление статьи.
     * Доступ разрешён только автору статьи.
     */
    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_USER') and (@articleService.findById(#id).author.username == principal.name)")
    public String updateArticle(@PathVariable Long id,
                                @RequestParam("title") String title,
                                @RequestParam("description") String description,
                                @RequestParam("body") String body,
                                @RequestParam("tagIds") List<Long> tagIds,
                                Principal principal) {
        Article existingArticle = articleService.findById(id);

        // Обновляем поля
        existingArticle.setTitle(title);
        existingArticle.setDescription(description);
        existingArticle.setContent(body);
        List<Tag> foundTags = tagService.findByIds(tagIds);
        existingArticle.setTags(new HashSet<>(foundTags));

        articleService.updateArticle(id, existingArticle);
        return "redirect:/articles";
    }

    /**
     * POST /articles/delete/{id}
     * Удаление статьи.
     * Доступ разрешён, если текущий пользователь является автором статьи или имеет роль ADMIN.
     */
    @PreAuthorize("hasRole('ROLE_USER') and ((@articleService.findById(#id).author.username == principal.name) or hasRole('ROLE_ADMIN'))")
    @PostMapping("/delete/{id}")
    public String deleteArticle(@PathVariable Long id) {
        articleService.deleteArticle(id);
        return "redirect:/articles";
    }
}
