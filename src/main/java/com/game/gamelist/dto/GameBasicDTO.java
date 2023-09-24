package com.game.gamelist.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class GameBasicDTO {
    private Long id;
    private String name;
    private String imageURL;
    private String bannerURL;
}
