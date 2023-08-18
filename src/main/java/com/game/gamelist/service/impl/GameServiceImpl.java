package com.game.gamelist.service.impl;

import com.game.gamelist.entity.Game;
import com.game.gamelist.model.GameQueryFilters;
import com.game.gamelist.repository.GameRepository;
import com.game.gamelist.service.GameService;
import com.game.gamelist.specification.GameSpecification;
import com.game.gamelist.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private static final int MIN_LIMIT = 1;
    private static final int MAX_LIMIT = 35;
    private final GameRepository gameRepository;

    @Override
    public List<Game> getAllGames(GameQueryFilters gameQueryFilters) {
        if(gameQueryFilters == null) {
            return gameRepository.findAll(Pageable.ofSize(20)).getContent();
        }

        Specification<Game> gameSpecification = new GameSpecification(gameQueryFilters);
        Pageable pageable = Pageable.ofSize(clampLimit(gameQueryFilters.getLimit())).withPage(gameQueryFilters.getOffset());
        return gameRepository.findAll(gameSpecification, pageable).getContent();
    }

    private int clampLimit(int limit) {
        return Utils.clamp(limit, MIN_LIMIT, MAX_LIMIT);
    }
}
