package com.example.vlogi.controllers;

import com.example.vlogi.entity.Tag;
import com.example.vlogi.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tags")
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    // GET /tags — отображение списка тегов
    @GetMapping
    public String showTags(Model model) {
        List<Tag> tags = tagService.findAll();
        model.addAttribute("tags", tags);
        return "tags/list"; // шаблон src/main/resources/templates/tags/list.html
    }

    // GET /tags/add — отображение формы для добавления нового тега
    @GetMapping("/add")
    public String showAddTagForm(Model model) {
        model.addAttribute("tag", new Tag());
        return "tags/add"; // шаблон src/main/resources/templates/tags/add.html
    }

    // POST /tags/add — обработка создания нового тега
    @PostMapping("/add")
    public String createTag(@ModelAttribute("tag") Tag tag) {
        tagService.createTag(tag);
        return "redirect:/tags";
    }

    // GET /tags/edit/{id} — отображение формы редактирования тега
    @GetMapping("/edit/{id}")
    public String showEditTagForm(@PathVariable Long id, Model model) {
        Tag tag = tagService.findById(id);
        if (tag == null) {
            return "redirect:/tags";
        }
        model.addAttribute("tag", tag);
        return "tags/edit"; // шаблон src/main/resources/templates/tags/edit.html
    }

    // POST /tags/edit/{id} — обработка обновления тега
    @PostMapping("/edit/{id}")
    public String updateTag(@PathVariable Long id, @ModelAttribute("tag") Tag tag) {
        tagService.updateTag(id, tag);
        return "redirect:/tags";
    }

    // POST /tags/delete/{id} — удаление тега
    @PostMapping("/delete/{id}")
    public String deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return "redirect:/tags";
    }
}
