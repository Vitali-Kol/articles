package com.example.vlogi.services;

import com.example.vlogi.entity.Tag;
import java.util.List;

public interface TagService {
    List<Tag> findAll();
    Tag findById(Long id);
    Tag createTag(Tag tag);
    void deleteTag(Long id);
    void updateTag(Long id, Tag updatedTag);
    List<Tag> findByIds(List<Long> ids);
}
