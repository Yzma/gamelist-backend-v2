package com.game.gamelist.service.impl;

import com.game.gamelist.entity.Game;
import com.game.gamelist.model.GameQueryFilters;
import com.game.gamelist.repository.GameRepository;
import com.game.gamelist.service.GameService;
import com.game.gamelist.specification.GameSpecification;
import com.game.gamelist.utils.Utils;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.Tuple;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {

    private static final int DEFAULT_QUERY_LIMIT = 10;
    private static final int MIN_QUERY_LIMIT = 1;
    private static final int MAX_QUERY_LIMIT = 35;
    private final GameRepository gameRepository;

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public List<Game> getAllGames(GameQueryFilters gameQueryFilters) {
        if(gameQueryFilters == null) {
            gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setLimit(DEFAULT_QUERY_LIMIT);
            gameQueryFilters.setSortBy("name");
        }

        // Ensure a limit is set, we don't want to fetch too many rows
        gameQueryFilters.setLimit(clampLimit(gameQueryFilters.getLimit()));

         Specification<Game> gameSpecification = new GameSpecification(gameQueryFilters);
//        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
//        CriteriaQuery<Tuple> cq = cb.createTupleQuery();
//        Root<Game> root = cq.from(Game.class);
//
//        Predicate specificationPredicate = gameSpecification.toPredicate(root, cq, cb);
//
//        Query query  = entityManager.createQuery(cq.where(specificationPredicate));
//
//        List l = query.getResultList();
//        System.out.println("List:");
//        System.out.println(l.toString());
//        for(Object k : l) {
//            System.out.println(k.toString());
//        }
       // Pageable pageable = Pageable.ofSize(clampLimit(gameQueryFilters.getLimit())).withPage(gameQueryFilters.getOffset());
        List<Game> please = gameRepository.findAll(gameSpecification);
        //System.out.println(please.get(0).getGenres());
        return  please;
    }

    private int clampLimit(int limit) {
        return Utils.clamp(limit, MIN_QUERY_LIMIT, MAX_QUERY_LIMIT);
    }
}
