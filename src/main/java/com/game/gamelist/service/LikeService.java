package com.game.gamelist.service;

import com.game.gamelist.entity.User;
import com.game.gamelist.projection.LikeEntityView;

public interface LikeService {
    LikeEntityView createLike(User principle, Long interactiveEntityId);

    void deleteLikeById(User principle, Long interactiveEntityId);
}
