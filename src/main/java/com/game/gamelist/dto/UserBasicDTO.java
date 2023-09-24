package com.game.gamelist.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class UserBasicDTO {
    private Long id;
    private String username;
    private String bannerPicture;
    private String userPicture;
}
