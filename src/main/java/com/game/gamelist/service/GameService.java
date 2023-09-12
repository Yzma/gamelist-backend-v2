package com.game.gamelist.service;

import com.game.gamelist.dto.GameDTO;
import com.game.gamelist.entity.User;
import com.game.gamelist.model.GameQueryFilters;

import java.util.List;

public interface GameService {
    List<GameDTO> getAllGames(GameQueryFilters gameQueryFilters, User principal);
}
