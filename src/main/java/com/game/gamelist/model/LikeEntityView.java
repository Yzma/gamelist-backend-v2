package com.game.gamelist.model;

import com.game.gamelist.entity.InteractiveEntity;
import com.game.gamelist.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


public interface LikeEntityView {
    Long getId();

    UserBasicView getUser();

//    InteractiveEntityView getInteractiveEntity();

    LocalDateTime getUpdatedAt();

    void setInteractiveEntityId(Long id);

    void setId(Long id);

    void setUser(User user);
}
