package com.game.gamelist.specification;

import com.game.gamelist.entity.Game;
import com.game.gamelist.entity.Genre;
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

        if (gameQueryFilters.getGenres() != null && !gameQueryFilters.getGenres().isEmpty()) {
            predicates.add(createInclusionFilter2(root, query, cb, gameQueryFilters.getGenres(), "genres"));
        }
        // Inclusion
//        if (gameQueryFilters.getGenres() != null && !gameQueryFilters.getGenres().isEmpty()) {
//            predicates.add(createInclusionFilter(root, query, cb, gameQueryFilters.getGenres(), "genres"));
//        }
//
//        if (gameQueryFilters.getPlatforms() != null && !gameQueryFilters.getPlatforms().isEmpty()) {
//            predicates.add(createInclusionFilter(root, query, cb, gameQueryFilters.getPlatforms(), "platforms"));
//        }
//
//        if (gameQueryFilters.getTags() != null && !gameQueryFilters.getTags().isEmpty()) {
//            predicates.add(createInclusionFilter(root, query, cb, gameQueryFilters.getTags(), "tags"));
//        }

        //predicates.add(createInclusionFilter1(root, query, cb, gameQueryFilters.getGenres(), gameQueryFilters.getPlatforms(), gameQueryFilters.getTags()));
        // createInclusionFilter1(root, query, cb, gameQueryFilters.getGenres(), gameQueryFilters.getPlatforms(), gameQueryFilters.getTags());
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
                case "newest_releases" -> query.orderBy(cb.desc(root.get("releaseDate")));
                case "oldest_releases" -> query.orderBy(cb.asc(root.get("releaseDate")));
                case "avg_score" -> query.orderBy(cb.desc(root.get("avgScore")));
                case "total_rating" -> query.orderBy(cb.desc(root.get("totalRating")));
            }
        }
        return cb.and(predicates.toArray(Predicate[]::new));
    }

    private Predicate createInclusionFilter1(Root<Game> root, CriteriaQuery<?> query, CriteriaBuilder cb,
                                             List<String> toIncludeGenres, List<String> toIncludePlatforms, List<String> toIncludeTags) {

        Subquery<Long> subquery = query.subquery(Long.class);
        Root<Game> subqueryRoot = subquery.from(Game.class);

        Join<Game, Genre> genreJoin = subqueryRoot.join("genres");
        subquery.select(subqueryRoot.get("id"))
                .where(genreJoin.get("name").in(toIncludeGenres))
                .groupBy(subqueryRoot.get("id"))
                .having(cb.equal(cb.count(genreJoin.get("name")), toIncludeGenres.size()));

        return cb.and(root.get("id").in(subquery));
    }

    private Predicate createInclusionFilter2(Root<Game> root, CriteriaQuery<?> query, CriteriaBuilder cb, List<String> toInclude, String tableName) {

        Subquery<Long> subquery = query.subquery(Long.class);
        Root<Game> subqueryRoot = subquery.from(Game.class);

        Join<Game, ?> genreJoin = subqueryRoot.join(tableName);
        subquery.select(subqueryRoot.get("id"))
                .where(genreJoin.get("name").in(toInclude))
                .groupBy(subqueryRoot.get("id"))
                .having(cb.equal(cb.count(genreJoin.get("name")), toInclude.size()));

        return cb.and(root.get("id").in(subquery));
    }

    private void createInclusionFilter1_old(Root<Game> root, CriteriaQuery<?> query, CriteriaBuilder cb,
                                            List<String> toIncludeGenres, List<String> toIncludePlatforms, List<String> toIncludeTags) {
        Subquery<String> subquery = query.subquery(String.class);
        Root<Game> subqueryRoot = subquery.from(Game.class);
        Join<Game, Genre> subqueryJoin = subqueryRoot.join("genres");

        // Group by genre name and apply HAVING clause
        subquery.select(subqueryJoin.get("name"))
                .groupBy(subqueryJoin.get("name"))
                .having(cb.equal(cb.count(subqueryJoin.get("name")), 1));

        // Use the result of the subquery in the IN condition
        query.where(root.get("genres").get("name").in(subquery), root.get("genres").get("name").in(toIncludeGenres));
//        List<Predicate> predicates = new ArrayList<>(3);
//        if (toIncludeGenres != null) {
//            Join<Game, Genre> tableJoin = root.join("genres");
//            Predicate wherePredicate = tableJoin.get("name").in(toIncludeGenres);
//
//            query.where(wherePredicate);
//            query.having(cb.equal(cb.countDistinct(tableJoin.get("name")), toIncludeGenres.size()));
//            //predicates.add(cb.equal(cb.countDistinct(tableJoin.get("name")), toIncludeGenres.size()));
//        }


//
//        if (toIncludePlatforms != null) {
//            Join<Game, Genre> tableJoin = root.join("platforms");
//            Predicate wherePredicate = tableJoin.get("name").in(toIncludePlatforms);
//
//            query.where(wherePredicate);
//            query.having(query.getGroupRestriction(), cb.equal(cb.countDistinct(tableJoin.get("name")), toIncludePlatforms.size()));
//            //predicates.add(cb.equal(cb.countDistinct(tableJoin.get("name")), toIncludeGenres.size()));
//        }
//
//        if (toIncludeTags != null) {
//            Join<Game, Genre> tableJoin = root.join("genres");
//            Predicate wherePredicate = cb.and(tableJoin.get("name").in(toIncludeTags));
//
//            query.where(wherePredicate);
//            query.having(query.getGroupRestriction(), cb.equal(cb.countDistinct(tableJoin.get("name")), toIncludeTags.size()));
//            //predicates.add(cb.equal(cb.countDistinct(tableJoin.get("name")), toIncludeGenres.size()));
//        }
        query.groupBy(root.get("id"));

    }

    private Predicate createInclusionFilter(Root<Game> root, CriteriaQuery<?> query, CriteriaBuilder cb, List<String> toInclude, String tableName) {
        Join<Game, Genre> tableJoin = root.join(tableName);
        Predicate wherePredicate = cb.and(tableJoin.get("name").in(toInclude));


//        query.having(cb.equal(cb.countDistinct(tableJoin.get("name")), toInclude.size()));
//        query.groupBy(root.get("id"));

        return cb.and(wherePredicate);

        /*

        1 genre

        SELECT DISTINCT "games".* FROM "games"
        INNER JOIN "games_genres" ON "games_genres"."game_id" = "games"."id"
        INNER JOIN "genres" ON "genres"."id" = "games_genres"."genre_id"
        WHERE "genres"."name" = $1
        GROUP BY "games"."id"
        HAVING (COUNT(DISTINCT genres.name) = 1)
        ORDER BY "games"."name" ASC
        LIMIT $2
        OFFSET $3
        [["name", "Fighting"], ["LIMIT", 20], ["OFFSET", 0]]

        2 genres:

        SELECT DISTINCT "games".* FROM "games"
        INNER JOIN "games_genres" ON "games_genres"."game_id" = "games"."id"
        INNER JOIN "genres" ON "genres"."id" = "games_genres"."genre_id"
        WHERE "genres"."name" IN ($1, $2)
        GROUP BY "games"."id"
        HAVING (COUNT(DISTINCT genres.name) = 2)
        ORDER BY "games"."name" ASC
        LIMIT $3
        OFFSET $4
        [["name", "Fighting"], ["name", "Shooter"], ["LIMIT", 20], ["OFFSET", 0]]
        */


//        Join<Game, Genre> tableJoin = root.join(tableName);
//        return tableJoin.get("name").in(toInclude);
    }

    private Predicate createExclusionFilter(Root<Game> root, CriteriaQuery<?> query, CriteriaBuilder cb, List<String> toExclude, String tableName) {
        Subquery<Long> subquery = query.subquery(Long.class);
        Root<Game> subqueryGameRoot = subquery.from(Game.class);
        Join<Game, ?> tableJoin = subqueryGameRoot.join(tableName);

        subquery.select(subqueryGameRoot.get("id"))
                .where(tableJoin.get("name").in(toExclude));

        return cb.not(root.get("id").in(subquery));
    }
}
