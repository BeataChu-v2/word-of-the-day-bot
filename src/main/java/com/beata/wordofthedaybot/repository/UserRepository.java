package com.beata.wordofthedaybot.repository;

import com.beata.wordofthedaybot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByTgUserId(Long tgUserId);

    List<User> findAllByActiveTrue();
}
