package com.game.gamelist.service.impl;

import com.game.gamelist.entity.Game;
import com.game.gamelist.entity.UserGame;
import com.game.gamelist.model.GameQueryFilters;
import com.game.gamelist.repository.GameRepository;
import com.game.gamelist.service.GameService;
import com.game.gamelist.specification.GameSpecification;
import com.game.gamelist.utils.Utils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private static final int DEFAULT_LIMIT = 10;
    private static final int MIN_LIMIT = 1;
    private static final int MAX_LIMIT = 35;
    private final GameRepository gameRepository;

    @Autowired
    private EntityManager entityManager;

    @Override
    public List<Game> getAllGames(GameQueryFilters gameQueryFilters) {
        if(gameQueryFilters == null) {
            gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setLimit(DEFAULT_LIMIT);
            gameQueryFilters.setSortBy("name");
//            gameQueryFilters.setGenres(List.of("Music"));
        }

        // Ensure a limit is set, we don't want to fetch too many rows
        gameQueryFilters.setLimit(clampLimit(gameQueryFilters.getLimit()));

        Specification<Game> gameSpecification = new GameSpecification(gameQueryFilters);
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
        Root<Game> root = cq.from(Game.class);

        Predicate specificationPredicate = gameSpecification.toPredicate(root, cq, cb);

        Query query  = entityManager.createQuery(cq.where(specificationPredicate));

        List l = query.getResultList();
        System.out.println("List:");
        System.out.println(l.toString());
        for(Object k : l) {
            System.out.println(k.toString());
        }
//        Pageable pageable = Pageable.ofSize(clampLimit(gameQueryFilters.getLimit())).withPage(gameQueryFilters.getOffset());
//        List<Game> please = gameRepository.findAll(gameSpecification);
        return new ArrayList<>();
    }

    private int clampLimit(int limit) {
        return Utils.clamp(limit, MIN_LIMIT, MAX_LIMIT);
    }
}
