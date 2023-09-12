package com.game.gamelist.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
public class GameDTO {
    private Long id;
    private String name;
    private double avgScore;
    private String imageURL;
    private String bannerURL;
    private LocalDateTime releaseDate;
    private List<String> platforms;
    private List<String> tags;
    private List<String> genres;
    private boolean gameAdded;
    private boolean gameLiked;
}
