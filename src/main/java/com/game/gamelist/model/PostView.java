package com.game.gamelist.model;

import java.time.LocalDateTime;
import java.util.List;

public interface PostView {
    Long getId();

    UserBasicView getUser();

    String getText();

    LocalDateTime getCreatedAt();

    List<LikeEntityView> getLikes();

    void setText(String text);
}
