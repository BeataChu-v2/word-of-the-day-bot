package com.beata.wordofthedaybot.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tg_user_id", nullable = false, unique = true)
    private Long tgUserId;

    @Setter
    @Column(name = "username")
    private String username;

    @Setter
    @Column(name = "first_name")
    private String firstName;

    @Column(
            name = "started_at",
            nullable = false,
            insertable = false,
            updatable = false
    )
    private LocalDateTime startedAt;

    @Column(
            name = "created_at",
            nullable = false,
            insertable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Setter
    @Column(name = "is_active", nullable = false)
    private boolean active = true;

    public User(
            Long tgUserId,
            String username,
            String firstName) {
        this.tgUserId = tgUserId;
        this.username = username;
        this.firstName = firstName;
    }

    public void activate() {
        this.active = true;
    }
    public void deactivate() {
        this.active = false;
    }
}
