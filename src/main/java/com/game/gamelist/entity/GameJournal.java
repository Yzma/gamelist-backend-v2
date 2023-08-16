package com.game.gamelist.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;


@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity(name = "game_journals")
public class GameJournal implements LikeableEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String content;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JsonIgnoreProperties("game_journals")
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "gameJournal", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("gameJournal")
    private List<LikeEntity> likes = new ArrayList<>();


}
