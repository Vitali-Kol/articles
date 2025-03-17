package com.example.vlogi.controllers;

import com.example.vlogi.entity.Tag;
import com.example.vlogi.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    // ========================
    // REST API endpoints (JSON)
    // ========================

    // GET /tags/api — вернуть список тегов в JSON
    @GetMapping("/api")
    @ResponseBody
    public List<Tag> getAllTagsApi() {
        return tagService.findAll();
    }

    // POST /tags/api — создать новый тег (JSON)
    @PostMapping("/api")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Tag createTagApi(@RequestBody Tag tag) {
        return tagService.createTag(tag);
    }

    // DELETE /tags/api/{id} — удалить тег (JSON)
    @DeleteMapping("/api/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTagApi(@PathVariable Integer id) {
        tagService.deleteTag(id);
    }

    // ========================
    // Представления (Thymeleaf-шаблоны)
    // ========================

    // GET /tags — отображение списка тегов
    @GetMapping
    public String showTags(Model model) {
        List<Tag> tags = tagService.findAll();
        model.addAttribute("tags", tags);
        return "tags/list"; // шаблон src/main/resources/templates/tags/list.html
    }
}
