package com.game.gamelist.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class GameDTO {
    private Long id;
    private String name;
    private double avgScore;
    private String imageURL;
    private LocalDateTime releaseDate;
    private List<String> platforms;
    private List<String> tags;
    private List<String> genres;

}
