package com.beata.wordofthedaybot.repository.word;

import com.beata.wordofthedaybot.entity.word.Collocation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollocationRepository
extends JpaRepository<Collocation, Long> {
    List<Collocation> findAllByWord_IdOrderBySortOrderAsc(Long wordId);
}
