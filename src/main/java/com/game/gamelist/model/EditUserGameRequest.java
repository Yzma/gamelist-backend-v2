package com.game.gamelist.model;

import com.game.gamelist.entity.GameStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class EditUserGameRequest {
    private Long gameId;
    private GameStatus gameStatus;
    private String gameNote;
    private Boolean isPrivate;
    private Integer rating;
    private LocalDateTime completedDate;
    private LocalDateTime startDate;
}
