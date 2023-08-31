package com.game.gamelist.service.impl;

import com.game.gamelist.dto.GameDTO;
import com.game.gamelist.entity.Game;
import com.game.gamelist.mapper.GameMapper;
import com.game.gamelist.model.GameQueryFilters;
import com.game.gamelist.service.GameService;
import com.game.gamelist.specification.GameSpecification;
import com.game.gamelist.utils.Utils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private static final int DEFAULT_QUERY_LIMIT = 10;
    private static final int MIN_QUERY_LIMIT = 1;
    private static final int MAX_QUERY_LIMIT = 35;

    private final GameMapper gameMapper;
    private final EntityManager em;

    @Override
    public List<GameDTO> getAllGames(GameQueryFilters gameQueryFilters) {
        if (gameQueryFilters == null) {
            gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setLimit(DEFAULT_QUERY_LIMIT);
            gameQueryFilters.setSortBy("name");
        }

        // Ensure a limit is set, we don't want to fetch too many rows
        gameQueryFilters.setLimit(clampLimit(gameQueryFilters.getLimit()));

        List<Game> foundGames = getQuery(gameQueryFilters);
        return gameMapper.gamesToGameDTOs(foundGames);
    }

    private List<Game> getQuery(GameQueryFilters gameQueryFilters) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Game> query = builder.createQuery(Game.class);
        Root<Game> root = query.from(Game.class);

        Specification<Game> gameSpecification = new GameSpecification(gameQueryFilters);

        query.select(root);
        query.where(gameSpecification.toPredicate(root, query, builder));

        TypedQuery<Game> typedQuery = em.createQuery(query);
        typedQuery.setMaxResults(gameQueryFilters.getLimit());
        typedQuery.setFirstResult(gameQueryFilters.getOffset());
        return typedQuery.getResultList();
    }

    private int clampLimit(int limit) {
        return Utils.clamp(limit, MIN_QUERY_LIMIT, MAX_QUERY_LIMIT);
    }
}
