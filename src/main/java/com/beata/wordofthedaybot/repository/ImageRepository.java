package com.beata.wordofthedaybot.repository;

import com.beata.wordofthedaybot.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends
        JpaRepository<Image, Long> {
    List<Image> findAllByWord_IdOrderBySortOrderAsc(Long wordId);
}
