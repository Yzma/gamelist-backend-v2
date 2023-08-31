package com.game.gamelist.model;

import com.game.gamelist.entity.GameStatus;
import com.game.gamelist.entity.UserGame;

import java.time.LocalDateTime;
import java.util.List;

public interface StatusUpdateView {
    Long getId();

    UserGameBasicView getUserGame();

    GameStatus getGameStatus();

    LocalDateTime getCreatedAt();

    List<LikeEntityView> getLikes();

    List<CommentView> getComments();
}
