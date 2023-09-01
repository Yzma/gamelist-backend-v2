package com.game.gamelist.service.impl;

import com.game.gamelist.entity.User;
import com.game.gamelist.projection.FollowView;
import com.game.gamelist.projection.UserBasicView;
import com.game.gamelist.service.FollowService;

public class FollowServiceImpl implements FollowService {
    @Override
    public UserBasicView createFollow(User principle, Long userId) {
        return null;
    }

    @Override
    public UserBasicView removeFollow(User principle, Long userId) {
        return null;
    }

    @Override
    public UserBasicView removeFollower(User principle, Long userId) {
        return null;
    }

    @Override
    public FollowView getAllFollows(User principle) {
        return null;
    }
}
