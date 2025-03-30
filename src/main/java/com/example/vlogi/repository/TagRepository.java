package com.example.vlogi.repository;

import com.example.vlogi.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByName(String name);
    List<Tag> findAllByIdIn(List<Long> ids);
    boolean existsByName(String name);

}
