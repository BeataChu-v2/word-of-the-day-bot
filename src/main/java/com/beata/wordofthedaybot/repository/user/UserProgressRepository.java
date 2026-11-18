package com.beata.wordofthedaybot.repository.user;

import com.beata.wordofthedaybot.entity.user.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserProgressRepository
extends JpaRepository<UserProgress, Long> {}
