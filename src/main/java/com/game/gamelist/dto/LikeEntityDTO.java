package com.game.gamelist.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
public class LikeEntityDTO {
    private Long id;
    private UserBasicDTO user;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
}
