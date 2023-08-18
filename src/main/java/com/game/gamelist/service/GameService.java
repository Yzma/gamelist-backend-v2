package com.game.gamelist.service;

import com.game.gamelist.entity.Game;
import com.game.gamelist.model.GameQueryFilters;

import java.util.List;

public interface GameService {
    List<Game> getAllGames(GameQueryFilters gameQueryFilters);
}
