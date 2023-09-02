package com.game.gamelist.model;

import com.game.gamelist.projection.UserBasicView;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public class MockUserBasicView implements UserBasicView {
    private Long id;
    private String username;
    private String bannerPicture;
    private String userPicture;
}
