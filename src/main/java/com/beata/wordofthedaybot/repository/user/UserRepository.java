package com.beata.wordofthedaybot.repository.user;

import com.beata.wordofthedaybot.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByTgUserId(Long tgUserId);

    List<User> findAllByActiveTrue();
}
