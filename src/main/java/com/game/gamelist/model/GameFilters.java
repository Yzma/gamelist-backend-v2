package com.game.gamelist.model;

import java.util.List;

public record GameFilters(List<String> genres, List<String> platforms, List<String> tags, int furthestYear) {
}
