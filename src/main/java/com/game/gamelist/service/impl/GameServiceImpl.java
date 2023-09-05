package com.game.gamelist.service.impl;

import com.game.gamelist.dto.GameDTO;
import com.game.gamelist.entity.Game;
import com.game.gamelist.entity.User;
import com.game.gamelist.mapper.GameMapper;
import com.game.gamelist.model.GameQueryFilters;
import com.game.gamelist.repository.GameRepository;
import com.game.gamelist.repository.LikeRepository;
import com.game.gamelist.repository.UserGameRepository;
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
    private final UserGameRepository userGameRepository;
    private final LikeRepository likeRepository;
    private final GameMapper gameMapper;
    private final EntityManager em;

    @Override
    public List<GameDTO> getAllGames(GameQueryFilters gameQueryFilters, User principal) {
        if (gameQueryFilters == null) {
            gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setLimit(DEFAULT_QUERY_LIMIT);
            gameQueryFilters.setSortBy("name");
        }

        // Ensure a limit is set, we don't want to fetch too many rows
        gameQueryFilters.setLimit(clampLimit(gameQueryFilters.getLimit()));

        Specification<Game> gameSpecification = new GameSpecification(gameQueryFilters);

        TypedQuery<Game> foundGames = getQuery(gameSpecification, Game.class);
        foundGames.setFirstResult(gameQueryFilters.getOffset());
        foundGames.setMaxResults(gameQueryFilters.getLimit());

        List<GameDTO> gameDTOs = gameMapper.gamesToGameDTOs(foundGames.getResultList());

        for (GameDTO gameDTO : gameDTOs) {
            if (principal == null) {
                gameDTO.setGameAdded(false);
                gameDTO.setGameLiked(false);
                continue;
            }

            gameDTO.setGameAdded(userGameRepository.existsByGameIdAndUserId(gameDTO.getId(), principal.getId()));
            gameDTO.setGameLiked(likeRepository.existsByUserIdAndInteractiveEntityId( principal.getId(), gameDTO.getId()));
        }

        return gameDTOs;
    }

    private <T> TypedQuery<T> getQuery(Specification<T> specification, Class<T> clazz) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<T> query = builder.createQuery(clazz);
        Root<T> root = query.from(clazz);

        query.select(root);
        if (specification != null) {
            query.where(specification.toPredicate(root, query, builder));
        }

        return em.createQuery(query);
    }

    private int clampLimit(int limit) {
        return Utils.clamp(limit, MIN_QUERY_LIMIT, MAX_QUERY_LIMIT);
    }
}
