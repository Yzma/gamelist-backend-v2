package com.game.gamelist.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
public class PostDTO {

    private Long id;
    private UserBasicDTO user;
    private String text;
    private LocalDateTime createdAt;
    private List<LikeEntityDTO> likes;
    private List<CommentDTO> comments;

}
