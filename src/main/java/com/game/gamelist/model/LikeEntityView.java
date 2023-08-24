package com.game.gamelist.model;

import java.time.LocalDateTime;

public interface LikeEntityView {
    Long getId();

    UserBasicView getUser();

    LocalDateTime getUpdatedAt();

    void setUser(UserBasicView user);
}
