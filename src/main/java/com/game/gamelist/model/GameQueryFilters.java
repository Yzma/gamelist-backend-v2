package com.game.gamelist.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class GameQueryFilters {

    @Data
    @NoArgsConstructor
    public static class GameQueryPaginationOptions {
        private int lastId = -1;
        private String lastName = null;
        private long lastReleaseDateEpoch = -1;
        private double lastAverageScore = -1;
        private int lastTotalRating = -1;
    }

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

    private GameQueryPaginationOptions gameQueryPaginationOptions;
}
