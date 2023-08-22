package com.game.gamelist.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto{

    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String text;
    private List<LikeEntityView> likes;
}