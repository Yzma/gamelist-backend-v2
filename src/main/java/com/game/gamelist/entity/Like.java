package com.game.gamelist.entity;

import java.time.LocalDateTime;

public interface Like {
    Long getId();
    User getUser();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();
}
