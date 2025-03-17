package com.example.vlogi.services;

import com.example.vlogi.entity.Tag;
import com.example.vlogi.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Autowired
    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public List<Tag> findAll() {
        return tagRepository.findAll();
    }

    @Override
    public Tag findById(Integer id) {
        return tagRepository.findById(id).orElse(null);
    }

    @Override
    public Tag createTag(Tag tag) {
        try {
            // Проверка, существует ли уже тег с таким именем
            Optional<Tag> existingTag = tagRepository.findByName(tag.getName());
            if (existingTag.isPresent()) {
                // Возвращаем существующий тег, если такой уже есть
                return existingTag.get();
            }
            // Если тег уникален, сохраняем его
            return tagRepository.save(tag);
        } catch (DataIntegrityViolationException e) {
            // Если происходит ошибка уникальности, возвращаем существующий тег
            return tagRepository.findByName(tag.getName()).orElse(null);
        }
    }

    @Override
    public void deleteTag(Integer id) {
        tagRepository.deleteById(id);
    }

    @Override
    public void updateTag(Integer id, Tag updatedTag) {
        // Ищем тег по ID
        Optional<Tag> existingTagOptional = tagRepository.findById(id);
        if (existingTagOptional.isPresent()) {
            Tag existingTag = existingTagOptional.get();
            // Обновляем только изменяемые поля
            existingTag.setName(updatedTag.getName());
            // Сохраняем обновленный тег
            tagRepository.save(existingTag);
        }
    }

    @Override
    public List<Tag> findByIds(List<Long> ids) {
        // Метод возвращает список тегов по списку идентификаторов
        return tagRepository.findAllByIdIn(ids);
    }
}
