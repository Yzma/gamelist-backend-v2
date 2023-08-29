package com.game.gamelist.service.impl;

import com.game.gamelist.dto.GameDTO;
import com.game.gamelist.entity.Game;
import com.game.gamelist.mapper.GameMapper;
import com.game.gamelist.model.GameQueryFilters;
import com.game.gamelist.repository.GameRepository;
import com.game.gamelist.service.GameService;
import com.game.gamelist.specification.GameSpecification;
import com.game.gamelist.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private static final int DEFAULT_QUERY_LIMIT = 10;
    private static final int MIN_QUERY_LIMIT = 1;
    private static final int MAX_QUERY_LIMIT = 35;
    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    @Override
    public List<GameDTO> getAllGames(GameQueryFilters gameQueryFilters) {
        if (gameQueryFilters == null) {
            gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setLimit(DEFAULT_QUERY_LIMIT);
            gameQueryFilters.setSortBy("name");
        }

        // Ensure a limit is set, we don't want to fetch too many rows
        gameQueryFilters.setLimit(clampLimit(gameQueryFilters.getLimit()));

        Specification<Game> gameSpecification = new GameSpecification(gameQueryFilters);
        Pageable pageable = Pageable.ofSize(clampLimit(gameQueryFilters.getLimit())).withPage(gameQueryFilters.getOffset());
        List<Game> foundGames = gameRepository.findAll(gameSpecification, pageable).getContent();

        return gameMapper.gamesToGameDTOs(foundGames);
    }

    private int clampLimit(int limit) {
        return Utils.clamp(limit, MIN_QUERY_LIMIT, MAX_QUERY_LIMIT);
    }
}
