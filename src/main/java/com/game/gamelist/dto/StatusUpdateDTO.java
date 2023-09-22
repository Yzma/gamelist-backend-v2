package com.game.gamelist.dto;

import com.game.gamelist.entity.GameStatus;
import com.game.gamelist.projection.CommentView;
import com.game.gamelist.projection.LikeEntityView;
import com.game.gamelist.projection.StatusUpdateView;
import com.game.gamelist.projection.UserGameBasicView;

import java.time.LocalDateTime;
import java.util.List;

public class StatusUpdateDTO{

        private Long id;
        private UserGameBasicView userGame;
        private GameStatus gameStatus;
        private LocalDateTime createdAt;
        private List<LikeEntityView> likes;
        private List<CommentView> comments;
}
