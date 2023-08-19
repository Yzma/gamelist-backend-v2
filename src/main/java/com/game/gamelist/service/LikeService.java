package com.game.gamelist.service;

import com.game.gamelist.entity.InteractiveEntity;
import com.game.gamelist.entity.LikeEntity;
import com.game.gamelist.entity.User;

public interface LikeService {
    LikeEntity createLike(User principle, Long interactiveEntityId);

    void deleteLikeById(User principle, Long interactiveEntityId);
}
