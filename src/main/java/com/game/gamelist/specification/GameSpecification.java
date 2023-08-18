package com.game.gamelist.specification;

import com.game.gamelist.entity.Game;
import com.game.gamelist.entity.Genre;
import com.game.gamelist.entity.Platform;
import com.game.gamelist.entity.Tag;
import com.game.gamelist.model.GameQueryFilters;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class GameSpecification implements Specification<Game> {

    private final GameQueryFilters gameQueryFilters;

    @Override
    public Predicate toPredicate(Root<Game> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        List<Predicate> predicates = new ArrayList<>();
/*
        1 ITEM SELECTED:

        SELECT DISTINCT "games".* FROM "games"
        INNER JOIN "games_genres" ON "games_genres"."game_id" = "games"."id"
        INNER JOIN "genres" ON "genres"."id" = "games_genres"."genre_id"
        WHERE "genres"."name" = $1
        GROUP BY "games"."id"
        HAVING (COUNT(DISTINCT genres.name) = 1)
        ORDER BY "games"."name" ASC
        LIMIT $2
        OFFSET $3  [["name", "Fighting"], ["LIMIT", 20], ["OFFSET", 0]]

        2 ITEMS SELECTED:

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
        // Inclusion
        if(gameQueryFilters.getGenres() != null && !gameQueryFilters.getGenres().isEmpty()) {
            predicates.add(cb.and(genreJoin(root).get("name").in(gameQueryFilters.getGenres())));
        }

        if(gameQueryFilters.getPlatforms() != null && !gameQueryFilters.getPlatforms().isEmpty()) {
            predicates.add(cb.and(platformJoin(root).get("name").in(gameQueryFilters.getPlatforms())));
        }

        if(gameQueryFilters.getTags() != null && !gameQueryFilters.getTags().isEmpty()) {
            predicates.add(cb.and(tagJoin(root).get("name").in(gameQueryFilters.getTags())));
        }

        // Exclusion
        if(gameQueryFilters.getExcludedGenres() != null && !gameQueryFilters.getExcludedGenres().isEmpty()) {
//            Subquery<Long> subquery = query.subquery(Long.class);
//            Root<Game> subqueryGameRoot = subquery.from(Game.class);
//            Join<Game, Genre> genreJoin = subqueryGameRoot.join("genres");
//
//            subquery.select(subqueryGameRoot.get("id"))
//                    .where(genreJoin.get("name").in(gameQueryFilters.getExcludedGenres()));
//
//            Predicate notInExcludedGenres = cb.not(root.get("id").in(subquery));
//            predicates.add(notInExcludedGenres);
            predicates.add(createExclusionFilter(root, query, cb, gameQueryFilters.getExcludedGenres(), "genres"));
        }

        if(gameQueryFilters.getExcludedPlatforms() != null && !gameQueryFilters.getExcludedPlatforms().isEmpty()) {
//            Subquery<Long> subquery = query.subquery(Long.class);
//            Root<Game> subqueryGameRoot = subquery.from(Game.class);
//            Join<Game, Platform> platformJoin = subqueryGameRoot.join("platforms");
//
//            subquery.select(subqueryGameRoot.get("id"))
//                    .where(platformJoin.get("name").in(gameQueryFilters.getExcludedPlatforms()));
//
//            Predicate notInExcludedPlatforms = cb.not(root.get("id").in(subquery));
//            predicates.add(notInExcludedPlatforms);
            predicates.add(createExclusionFilter(root, query, cb, gameQueryFilters.getExcludedPlatforms(), "platforms"));
        }

        if(gameQueryFilters.getExcludedTags() != null && !gameQueryFilters.getExcludedTags().isEmpty()) {
//            Subquery<Long> subquery = query.subquery(Long.class);
//            Root<Game> subqueryGameRoot = subquery.from(Game.class);
//            Join<Game, Tag> platformJoin = subqueryGameRoot.join("tags");
//
//            subquery.select(subqueryGameRoot.get("id"))
//                    .where(platformJoin.get("name").in(gameQueryFilters.getExcludedTags()));
//
//            Predicate notInExcludedTags = cb.not(root.get("id").in(subquery));
//            predicates.add(notInExcludedTags);
            predicates.add(createExclusionFilter(root, query, cb, gameQueryFilters.getExcludedTags(), "tags"));
        }
        // games_table.joins(table_type).where(table_type => { column_name => value }).having("COUNT(DISTINCT #{table_type}.#{column_name}) = ?", value.length).group("games.id")

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

        return cb.and(predicates.toArray(new Predicate[0]));
    }

//            if(gameQueryFilters.getExcludedPlatforms() != null && !gameQueryFilters.getExcludedPlatforms().isEmpty()) {
//        Subquery<Long> subquery = query.subquery(Long.class);
//        Root<Game> subqueryGameRoot = subquery.from(Game.class);
//        Join<Game, Platform> platformJoin = subqueryGameRoot.join("platforms");
//
//        subquery.select(subqueryGameRoot.get("id"))
//                .where(platformJoin.get("name").in(gameQueryFilters.getExcludedPlatforms()));
//
//        Predicate notInExcludedPlatforms = cb.not(root.get("id").in(subquery));
//        predicates.add(notInExcludedPlatforms);
//    }


    private Predicate createExclusionFilter(Root<Game> root, CriteriaQuery<?> query, CriteriaBuilder cb, List<String> toExclude, String tableName) {
        Subquery<Long> subquery = query.subquery(Long.class);
        Root<Game> subqueryGameRoot = subquery.from(Game.class);
        Join<Game, ?> tableJoin = subqueryGameRoot.join(tableName);

        subquery.select(subqueryGameRoot.get("id"))
                .where(tableJoin.get("name").in(toExclude));

        return cb.not(root.get("id").in(subquery));
    }

    private Join<Game,Genre> genreJoin(Root<Game> root){
        return root.join("genres");
    }

    private Join<Game, Platform> platformJoin(Root<Game> root){
        return root.join("platforms");
    }

    private Join<Game, Tag> tagJoin(Root<Game> root){
        return root.join("tags");
    }
}
