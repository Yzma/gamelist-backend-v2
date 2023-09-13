package com.game.gamelist.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@ToString
public class GameQueryFilters {

    private List<String> genres;
    private List<String> excludedGenres;
    private List<String> platforms;
    private List<String> excludedPlatforms;
    private List<String> tags;
    private List<String> excludedTags;
    private int year;
    private String sortBy;
    private String search;

    private int limit;
    private int offset;

    private int lastId;
    private String lastName;
    private double lastAverageScore;
    private int lastTotalRating;
}
