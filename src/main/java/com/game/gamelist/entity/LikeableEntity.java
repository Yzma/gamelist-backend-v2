package com.game.gamelist.entity;


import java.util.List;

public interface LikeableEntity {
    Long getId();
    List<LikeEntity> getLikes();
}
