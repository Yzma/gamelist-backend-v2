package com.game.gamelist.model;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentView {

    Long getId();

    UserBasicView getUser();

    String getText();

    LocalDateTime getCreatedAt();

    void setText(String text);

    List<LikeEntityView> getLikes();
    List<CommentView> getComments();
}
