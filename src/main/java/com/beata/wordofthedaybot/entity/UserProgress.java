package com.beata.wordofthedaybot.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "user_progress")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserProgress {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "last_sequence_number", nullable = false)
    private Integer lastSequenceNumber = 0;

    @Column(name = "last_sent_at")
    private LocalDateTime lastSentAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "last_quiz_sequence_number", nullable = false)
    private Integer lastQuizSequenceNumber = 0;

    public UserProgress(User user) {
        this.user = Objects.requireNonNull(user);
        this.lastSequenceNumber = 0;
    }

    public void markWordSent(Integer sequenceNumber) {
        if (sequenceNumber == null || sequenceNumber < 1) {
            throw new IllegalArgumentException(
                    "Sequence number must be positive"
            );
        }

        this.lastSequenceNumber = sequenceNumber;
        this.lastSentAt = LocalDateTime.now();
    }

    public void markQuizSent(Integer lastSequenceNumber) {
        if (lastSequenceNumber == null || lastSequenceNumber < 1
                || lastSequenceNumber % 6 != 0) {
            throw new IllegalArgumentException(
                    "Last sequence number must be positive and divide by 6"
            );
        }

        if (lastQuizSequenceNumber >= lastSequenceNumber) {
            throw new IllegalStateException("Quiz already sent");
        };

        this.lastQuizSequenceNumber = lastSequenceNumber;
    }

    public void resetProgress() {
        this.lastSequenceNumber = 0;
        this.lastSentAt = null;
    }

    @PrePersist
    protected void onCreate() {
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
