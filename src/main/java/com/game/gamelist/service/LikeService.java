package com.game.gamelist.service;

import com.game.gamelist.entity.InteractiveEntity;
import com.game.gamelist.entity.LikeEntity;
import com.game.gamelist.entity.User;
import com.game.gamelist.model.LikeEntityView;

public interface LikeService {
    LikeEntityView createLike(User principle, Long interactiveEntityId);

    void deleteLikeById(User principle, Long interactiveEntityId);
}
