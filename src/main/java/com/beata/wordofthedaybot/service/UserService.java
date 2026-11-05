package com.beata.wordofthedaybot.service;

import com.beata.wordofthedaybot.entity.User;
import com.beata.wordofthedaybot.entity.UserProgress;
import com.beata.wordofthedaybot.exception.UserNotRegisteredException;
import com.beata.wordofthedaybot.repository.UserRepository;
import com.beata.wordofthedaybot.repository.UserProgressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserProgressRepository userProgressRepository;

    public UserService(UserRepository userRepository, UserProgressRepository userProgressRepository) {
        this.userRepository = userRepository;
        this.userProgressRepository = userProgressRepository;
    }

    @Transactional
    public User registerUser(Long tgUserId, String username, String firstName) {
        return userRepository.findByTgUserId(tgUserId)
                .map(user -> {
                    user.setUsername(username);
                    user.setFirstName(firstName);
                    user.activate();

                    return userRepository.save(user);
                })
                .orElseGet(() -> {
                    User newUser = new User(tgUserId, username, firstName);
                    User savedUser = userRepository.save(newUser);

                    UserProgress progress = new UserProgress(savedUser);
                    userProgressRepository.save(progress);

                    return savedUser;
                });
    }

    @Transactional
    public boolean deactivateUser(Long tgUserId) {
        return userRepository.findByTgUserId(tgUserId)
                .map(user -> {
                    user.deactivate();
                    return true;
                })
                .orElse(false);
    }

    @Transactional
    public void deleteUser(Long tgUserId) {
        User user = userRepository.findByTgUserId(tgUserId)
                .orElseThrow(UserNotRegisteredException::new);

        userProgressRepository.deleteById(user.getId());
        userRepository.delete(user);
    }

    public void resetUserProgress(Long tgUserId) {
        User user = userRepository.findByTgUserId(tgUserId)
                .orElseThrow(UserNotRegisteredException::new);

        UserProgress progress = userProgressRepository.findById(user.getId())
                .orElseThrow(() -> new IllegalStateException("User progress not found"));
        progress.resetProgress();
    }

    @Transactional(readOnly = true)
    public List<User> getAllActiveUsers() {
        return userRepository.findAllByActiveTrue();
    }

}
