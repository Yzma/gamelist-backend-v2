package com.game.gamelist.model;

import com.game.gamelist.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@SuperBuilder
@Getter
@Setter
public class MockLikeEntitiyView implements LikeEntityView {

    private Long id;
    private UserBasicView user;
    private LocalDateTime updatedAt;

    @Override
    public void setUser(UserBasicView user) {
        this.user = user;
    }
}
