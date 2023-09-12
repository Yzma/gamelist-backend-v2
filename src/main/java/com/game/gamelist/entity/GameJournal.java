package com.game.gamelist.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

//@DiscriminatorValue("game_journal_type")


@Getter
@Setter
@SuperBuilder
@PrimaryKeyJoinColumn(name = "game_journal_id")
@NoArgsConstructor
@Entity(name = "game_journals")

public class GameJournal extends InteractiveEntity {

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("game_journals")
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
}

