package com.game.gamelist.projection;

import java.util.Set;

public interface FollowView {
    Long getId();

    Set<UserBasicView> getFollowers();

    Set<UserBasicView> getFollowing();

    void setFollowers(Set<UserBasicView> followers);

    void setFollowing(Set<UserBasicView> following);
}
