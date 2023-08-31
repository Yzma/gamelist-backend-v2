package com.game.gamelist.projection;

import com.game.gamelist.projection.UserBasicView;

import java.time.LocalDateTime;

public interface LikeEntityView {
    Long getId();

    UserBasicView getUser();

    LocalDateTime getUpdatedAt();

    void setUser(UserBasicView user);
}
