package com.game.gamelist.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.game.gamelist.entity.GameStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class EditUserGameRequest {
    private GameRequestBody game;
    private GameStatus gameStatus;
    private String gameNote;
    private Boolean isPrivate;
    private Integer rating;
    private LocalDateTime completedDate;
    private LocalDateTime startDate;

    @Getter
    public static class GameRequestBody {
        private Long id;

        @JsonCreator
        public GameRequestBody(@JsonProperty("id") Long id) {
            this.id = id;
        }
    }
}

