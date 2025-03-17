package com.example.vlogi.services;

import com.example.vlogi.entity.Tag;
import com.example.vlogi.repository.TagRepository;
import com.example.vlogi.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

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
        return tagRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));
    }

    @Override
    public Tag createTag(Tag tag) {
        if (tagRepository.findByName(tag.getName()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tag with this name already exists");
        }
        return tagRepository.save(tag);
    }

    @Override
    public void deleteTag(Integer id) {
        Tag existing = tagRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found"));
        tagRepository.delete(existing);
    }

    @Override
    public void updateTag(Integer id, Tag updatedTag) {
        
    }
}
