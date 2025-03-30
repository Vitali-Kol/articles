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
    public Tag findById(Long id) {
        return tagRepository.findById(id).orElse(null);
    }

    @Override
    public Tag createTag(Tag tag) {
        try {
            Optional<Tag> existingTag = tagRepository.findByName(tag.getName());
            if (existingTag.isPresent()) {
                return existingTag.get();
            }
            return tagRepository.save(tag);
        } catch (DataIntegrityViolationException e) {
            return tagRepository.findByName(tag.getName()).orElse(null);
        }
    }

    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    @Override
    public void updateTag(Long id, Tag updatedTag) {
        Optional<Tag> existingTagOptional = tagRepository.findById(id);
        if (existingTagOptional.isPresent()) {
            Tag existingTag = existingTagOptional.get();
            existingTag.setName(updatedTag.getName());
            tagRepository.save(existingTag);
        }
    }

    @Override
    public List<Tag> findByIds(List<Long> ids) {
        return tagRepository.findAllByIdIn(ids);
    }
}
