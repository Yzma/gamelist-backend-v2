package com.game.gamelist.dto;

import com.game.gamelist.entity.GameStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
public class StatusUpdateDTO{
        private Long id;
        private UserGameDTO userGame;
        private GameStatus gameStatus;
        private LocalDateTime createdAt;
        private List<LikeEntityDTO> likes;
        private List<CommentDTO> comments;
}
