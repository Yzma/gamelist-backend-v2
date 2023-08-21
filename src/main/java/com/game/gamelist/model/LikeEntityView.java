package com.game.gamelist.model;

import com.game.gamelist.entity.InteractiveEntity;
import com.game.gamelist.entity.User;

import java.time.LocalDateTime;

public interface LikeEntityView {
    Long getId();

    UserBasicView getUser();

//    InteractiveEntityView getInteractiveEntity();

    LocalDateTime getUpdatedAt();

}
