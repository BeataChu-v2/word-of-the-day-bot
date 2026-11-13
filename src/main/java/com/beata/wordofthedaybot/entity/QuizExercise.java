package com.beata.wordofthedaybot.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "quiz_exercises",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_quiz_exercises_order",
                        columnNames = {"quiz_id", "sort_order"}
                )
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "quiz_id", nullable = false)
    private Quiz quiz;

    @Column(name = "exercise_type", nullable = false, length = 30)
    private String exerciseType;

    @Column(nullable = false)
    private String instruction;

    @Column(nullable = false)
    private String content;

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder;
}
