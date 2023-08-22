package com.game.gamelist.model;

import java.util.List;

public interface PostView {
    Long getId();

    UserBasicView getUser();

    String getText();

    List<LikeEntityView> getLikes();

    void setText(String text);
}
