package com.beata.wordofthedaybot.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "words")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(name = "sequence_number", nullable = false, unique = true)
    private Integer sequenceNumber;

    @Setter
    @Column(name = "word", nullable = false, length = 100)
    private String word;

    @Setter
    @Column(name = "translation", nullable = false)
    private String translation;

    @Column(
            name = "created_at",
            nullable = false,
            insertable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    public Word(Integer sequenceNumber, String word, String translation) {
        this.sequenceNumber = sequenceNumber;
        this.word = word;
        this.translation = translation;
    }
}
