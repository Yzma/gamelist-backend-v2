package com.game.gamelist.specification;

import com.game.gamelist.entity.Game;
import com.game.gamelist.model.GameQueryFilters;
import jakarta.persistence.criteria.*;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
        if (gameQueryFilters.getGenres() != null && !gameQueryFilters.getGenres().isEmpty()) {
            predicates.add(createInclusionFilter(root, query, cb, gameQueryFilters.getGenres(), "genres"));
        }

        if (gameQueryFilters.getPlatforms() != null && !gameQueryFilters.getPlatforms().isEmpty()) {
            predicates.add(createInclusionFilter(root, query, cb, gameQueryFilters.getPlatforms(), "platforms"));
        }

        if (gameQueryFilters.getTags() != null && !gameQueryFilters.getTags().isEmpty()) {
            predicates.add(createInclusionFilter(root, query, cb, gameQueryFilters.getTags(), "tags"));
        }

        // Exclusion
        if (gameQueryFilters.getExcludedGenres() != null && !gameQueryFilters.getExcludedGenres().isEmpty()) {
            predicates.add(createExclusionFilter(root, query, cb, gameQueryFilters.getExcludedGenres(), "genres"));
        }

        if (gameQueryFilters.getExcludedPlatforms() != null && !gameQueryFilters.getExcludedPlatforms().isEmpty()) {
            predicates.add(createExclusionFilter(root, query, cb, gameQueryFilters.getExcludedPlatforms(), "platforms"));
        }

        if (gameQueryFilters.getExcludedTags() != null && !gameQueryFilters.getExcludedTags().isEmpty()) {
            predicates.add(createExclusionFilter(root, query, cb, gameQueryFilters.getExcludedTags(), "tags"));
        }

        if (gameQueryFilters.getSearch() != null) {
            predicates.add(cb.like(cb.lower(root.get("name")), "%" + gameQueryFilters.getSearch().toLowerCase() + "%"));
        }

        if (gameQueryFilters.getYear() != 0) {
            Expression<Integer> year = cb.function("DATE_PART", Integer.class, cb.literal("YEAR"), root.get("releaseDate"));
            predicates.add(cb.equal(year, gameQueryFilters.getYear()));
        }

        if (gameQueryFilters.getSortBy() != null) {
            switch (gameQueryFilters.getSortBy()) {
                case "name" -> query.orderBy(cb.asc(root.get("name")));
                case "name_desc" -> query.orderBy(cb.desc(root.get("name")));
                case "newest_releases" -> query.orderBy(cb.desc(root.get("releaseDate")));
                case "oldest_releases" -> query.orderBy(cb.asc(root.get("releaseDate")));
                case "avg_score" -> query.orderBy(cb.desc(root.get("avgScore")));
                case "lowest_avg_score" -> query.orderBy(cb.asc(root.get("avgScore")));
                case "total_rating" -> query.orderBy(cb.desc(root.get("totalRating")));
                case "lowest_total_rating" -> query.orderBy(cb.asc(root.get("totalRating")));
            }
        }
        return cb.and(predicates.toArray(Predicate[]::new));
    }

    private Predicate createInclusionFilter(Root<Game> root, CriteriaQuery<?> query, CriteriaBuilder cb, List<String> toInclude, String tableName) {
        Subquery<Long> subquery = query.subquery(Long.class);
        Root<Game> subqueryRoot = subquery.from(Game.class);

        Join<Game, ?> tableJoin = subqueryRoot.join(tableName);
        subquery.select(subqueryRoot.get("id"))
                .where(tableJoin.get("name").in(toInclude))
                .groupBy(subqueryRoot.get("id"))
                .having(cb.equal(cb.count(tableJoin.get("name")), toInclude.size()));

        return cb.and(root.get("id").in(subquery));
    }

    private Predicate createExclusionFilter(Root<Game> root, CriteriaQuery<?> query, CriteriaBuilder cb, List<String> toExclude, String tableName) {
        Subquery<Long> subquery = query.subquery(Long.class);
        Root<Game> subqueryRoot = subquery.from(Game.class);

        Join<Game, ?> tableJoin = subqueryRoot.join(tableName);
        subquery.select(subqueryRoot.get("id"))
                .where(tableJoin.get("name").in(toExclude));

        return cb.not(root.get("id").in(subquery));
    }
}
