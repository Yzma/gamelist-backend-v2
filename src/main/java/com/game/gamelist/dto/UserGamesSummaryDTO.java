package com.game.gamelist.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class UserGamesSummaryDTO {
    private List<GameDTO> playing;
    private int playingCount;

    private List<GameDTO> completed;
    private int completedCount;

    private List<GameDTO> paused;
    private int pausedCount;

    private List<GameDTO> planning;
    private int planningCount;

    private List<GameDTO> dropped;
    private int droppedCount;

    private List<GameDTO> justAdded;
    private int justAddedCount;

    private int totalCount;

    private String listsOrder;
}
