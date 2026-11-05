package com.beata.wordofthedaybot.repository;

import com.beata.wordofthedaybot.entity.Collocation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollocationRepository
extends JpaRepository<Collocation, Long> {
    List<Collocation> findAllByWord_IdOrderBySortOrderAsc(Long wordId);
}
