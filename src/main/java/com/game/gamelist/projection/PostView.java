package com.game.gamelist.projection;

import java.time.LocalDateTime;
import java.util.List;

public interface PostView {
    Long getId();

    UserBasicView getUser();

    String getText();

    LocalDateTime getCreatedAt();

    List<LikeEntityView> getLikes();

    List<CommentView> getComments();

    void setText(String text);
}
