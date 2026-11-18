package com.beata.wordofthedaybot.service.word;

import com.beata.wordofthedaybot.dto.WordContent;
import com.beata.wordofthedaybot.entity.user.User;
import com.beata.wordofthedaybot.entity.user.UserProgress;
import com.beata.wordofthedaybot.exception.NoMoreWordsException;
import com.beata.wordofthedaybot.exception.UserNotRegisteredException;
import com.beata.wordofthedaybot.repository.user.UserProgressRepository;
import com.beata.wordofthedaybot.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WordMessageService {

    private final UserRepository userRepository;
    private final UserProgressRepository userProgressRepository;
    private final WordContentService wordService;

    @Transactional(readOnly = true)
    public WordContent getNextWord(Long tgUserId) {
        User user = userRepository.findByTgUserId(tgUserId)
                .orElseThrow(() -> new UserNotRegisteredException(tgUserId));

        UserProgress progress = userProgressRepository.findById(user.getId())
                .orElseThrow(() -> new UserNotRegisteredException(tgUserId));

        Integer nextSequenceNumber = progress.getLastSequenceNumber() + 1;

        return wordService.findContentBySequenceNumber(nextSequenceNumber)
                .orElseThrow(NoMoreWordsException::new);
    }

    @Transactional
    public void markWordAsSent(Long tgUserId, Integer sequenceNumber) {
        User user = userRepository.findByTgUserId(tgUserId)
                .orElseThrow(() -> new UserNotRegisteredException(tgUserId));

        UserProgress progress = userProgressRepository.findById(user.getId())
                .orElseThrow(() ->
                        new IllegalStateException("User progress not found while sending"));

        Integer expectedSequenceNumber = progress.getLastSequenceNumber() + 1;

       if (!expectedSequenceNumber.equals(sequenceNumber)) {
           throw new IllegalArgumentException("Unexpected sequence number");
       }
       progress.markWordSent(sequenceNumber);
    }


}
