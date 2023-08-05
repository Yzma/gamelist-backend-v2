package com.game.gamelist.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity(name = "user_games")
public class UserGame {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "game_status")
    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;


    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "completed_date")
    private LocalDateTime completedDate;

    @Column(name = "private")
    private Boolean isPrivate;

    private Integer rating;

    @Column(name = "game_note")
    private String gameNote;


    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonBackReference
    @JsonIgnoreProperties("user_games")
    @JoinColumn(name = "game_id", referencedColumnName = "id")
    private Game game;

}
