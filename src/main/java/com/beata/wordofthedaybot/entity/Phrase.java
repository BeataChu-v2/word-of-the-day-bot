package com.beata.wordofthedaybot.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "phrases")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Phrase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "word_id", nullable = false)
    private Word word;

    @Setter
    @Column(name = "text", nullable = false, length = 500)
    private String text;

    @Setter
    @Column(name = "translation", nullable = false, length = 500)
    private String translation;

    @Setter
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    public Phrase(
            Word word,
            String text,
            String translation,
            Integer sortOrder
    ) {
        this.word = word;
        this.text = text;
        this.translation = translation;
        this.sortOrder = sortOrder;
    }
}
