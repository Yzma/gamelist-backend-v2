package com.game.gamelist.service;

import com.game.gamelist.entity.User;
import com.game.gamelist.projection.FollowView;
import com.game.gamelist.projection.UserBasicView;

public interface FollowService {
    UserBasicView createFollow(User principle, Long userId);

    UserBasicView removeFollow(User principle, Long userId);

    UserBasicView removeFollower(User principle, Long userId);

    FollowView getAllFollows(User principle);
}
