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
            Join<Game, Genre> join = root.join("genres", JoinType.INNER);

            /// This works!!!!
//            Join<Game, UserGame> join = root.join("userGames", JoinType.INNER);
//            join.alias("userGamess"); // Provide an alias for the join
//            query.multiselect(join.get("gameNote"));

            ////


            query.multiselect(root, join.get("name"));
      predicates.add(join.get("name").in(gameQueryFilters.getGenres()));

//            Join<Game, Genre> join = root.join("genres", JoinType.INNER);
//            join.alias("genre_alias"); // Provide an alias for the join
//            query.multiselect(join.get("name").alias("genreName"));
//            query.select(join.get("name"));
//            predicates.add(join.get("name").in(gameQueryFilters.getGenres()));
            // Select the 'name' column from the 'genres' table with alias


            //root.fetch("genres");
//            query.multiselect(join.get("name").alias("firstName"));
            //predicates.add(join.get("name").in(gameQueryFilters.getGenres()));

        }

        if(gameQueryFilters.getPlatforms() != null && !gameQueryFilters.getPlatforms().isEmpty()) {
            predicates.add(platformJoin(root).get("name").in(gameQueryFilters.getPlatforms()));
        }

        if(gameQueryFilters.getTags() != null && !gameQueryFilters.getTags().isEmpty()) {
            predicates.add(tagJoin(root).get("name").in(gameQueryFilters.getTags()));
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
