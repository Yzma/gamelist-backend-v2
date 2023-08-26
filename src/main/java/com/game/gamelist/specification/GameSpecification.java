package com.game.gamelist.specification;

import com.game.gamelist.entity.*;
import com.game.gamelist.model.GameQueryFilters;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;


import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class GameSpecification implements Specification<Game> {

    private final GameQueryFilters gameQueryFilters;

    @Override
    public Predicate toPredicate(@NonNull Root<Game> root, @NonNull CriteriaQuery<?> query, @NonNull CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();

        // Inclusion
        if(gameQueryFilters.getGenres() != null && !gameQueryFilters.getGenres().isEmpty()) {
//            Join<Game, Genre> genreJoin = root.join("genres", JoinType.INNER);
//            query.multiselect(root, genreJoin.get("name"));
//            predicates.add(genreJoin.get("name").in(gameQueryFilters.getGenres()));
            predicates.add(createInclusionFilter(root, query, gameQueryFilters.getGenres(), "genres"));
        }

        if(gameQueryFilters.getPlatforms() != null && !gameQueryFilters.getPlatforms().isEmpty()) {
//            Join<Game, Genre> platformJoin = root.join("platforms", JoinType.INNER);
//            query.multiselect(root, platformJoin.get("name"));
//            predicates.add(platformJoin.get("name").in(gameQueryFilters.getPlatforms()));
            predicates.add(createInclusionFilter(root, query, gameQueryFilters.getPlatforms(), "platforms"));
        }

        if(gameQueryFilters.getTags() != null && !gameQueryFilters.getTags().isEmpty()) {
//            Join<Game, Genre> tagJoin = root.join("tags", JoinType.INNER);
//            query.multiselect(root, tagJoin.get("name"));
//            predicates.add(tagJoin.get("name").in(gameQueryFilters.getTags()));
            predicates.add(createInclusionFilter(root, query, gameQueryFilters.getTags(), "tags"));
        }

        // Exclusion
        if(gameQueryFilters.getExcludedGenres() != null && !gameQueryFilters.getExcludedGenres().isEmpty()) {
            predicates.add(createExclusionFilter(root, query, cb, gameQueryFilters.getExcludedGenres(), "genres"));
        }

        if(gameQueryFilters.getExcludedPlatforms() != null && !gameQueryFilters.getExcludedPlatforms().isEmpty()) {
            predicates.add(createExclusionFilter(root, query, cb, gameQueryFilters.getExcludedPlatforms(), "platforms"));
        }

        if(gameQueryFilters.getExcludedTags() != null && !gameQueryFilters.getExcludedTags().isEmpty()) {
            predicates.add(createExclusionFilter(root, query, cb, gameQueryFilters.getExcludedTags(), "tags"));
        }

        if(gameQueryFilters.getSearch() != null) {
            predicates.add(cb.like(root.get("name"), "%" + gameQueryFilters.getSearch() + "%"));
        }

        if(gameQueryFilters.getYear() != 0) {
            Expression<Integer> year = cb.function("DATE_PART", Integer.class, cb.literal("YEAR"), root.get("releaseDate"));
            predicates.add(cb.equal(year, gameQueryFilters.getYear()));
        }

        if(gameQueryFilters.getSortBy() != null) {
            switch (gameQueryFilters.getSortBy()) {
                case "name" -> query.orderBy(cb.asc(root.get("name")));
                case "newest_releases" -> query.orderBy(cb.desc(root.get("releaseDate")));
                case "oldest_releases" -> query.orderBy(cb.asc(root.get("releaseDate")));
                case "avg_score" -> query.orderBy(cb.desc(root.get("avgScore")));
                case "total_rating" -> query.orderBy(cb.desc(root.get("totalRating")));
            }
        }

        return cb.and(predicates.toArray(Predicate[]::new));
    }

    private Predicate createInclusionFilter(Root<Game> root, CriteriaQuery<?> query, List<String> toInclude, String tableName) {
        Join<Game, Genre> tableJoin = root.join(tableName, JoinType.INNER);
        query.multiselect(root, tableJoin.get("name"));
        return tableJoin.get("name").in(toInclude);
    }

    private Predicate createExclusionFilter(Root<Game> root, CriteriaQuery<?> query, CriteriaBuilder cb, List<String> toExclude, String tableName) {
        Subquery<Long> subquery = query.subquery(Long.class);
        Root<Game> subqueryGameRoot = subquery.from(Game.class);
        Join<Game, ?> tableJoin = subqueryGameRoot.join(tableName);

        query.multiselect(root, tableJoin.get("name"));
        subquery.select(subqueryGameRoot.get("id"))
                .where(tableJoin.get("name").in(toExclude));

        return cb.not(root.get("id").in(subquery));
    }
}
