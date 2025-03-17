package com.example.vlogi.services;

import com.example.vlogi.entity.Tag;
import java.util.List;

public interface TagService {
    List<Tag> findAll();
    Tag findById(Integer id);
    Tag createTag(Tag tag);
    void deleteTag(Integer id);
    void updateTag(Integer id, Tag updatedTag);

}
