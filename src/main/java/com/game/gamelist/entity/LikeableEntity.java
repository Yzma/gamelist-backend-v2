package com.game.gamelist.entity;



import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.LocalDateTime;
import java.util.List;

public interface LikeableEntity {

    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
}