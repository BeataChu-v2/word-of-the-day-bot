package com.beata.wordofthedaybot.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "images")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "word_id", nullable = false)
    private Word word;

    @Setter
    @Column(name = "file_id", nullable = false, columnDefinition = "TEXT")
    private String fileId;

    @Setter
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;

    public Image(
            Word word,
            String fileId,
            Integer sortOrder
    ) {
        this.word = word;
        this.fileId = fileId;
        this.sortOrder = sortOrder;
    }
}
