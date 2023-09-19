package com.game.gamelist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity(name = "games")
@Table(indexes = {
        @Index(name = "nameIndex", columnList = "name"),
        @Index(name = "releaseDateIndex", columnList = "releaseDate"),
        @Index(name = "avgScoreIndex", columnList = "avg_score"),
        @Index(name = "totalRatingIndex", columnList = "total_rating"),

        @Index(name = "mulitIndexNameIdAsc", columnList = "name ASC, id"),
        @Index(name = "mulitIndexNameIdDesc", columnList = "name DESC, id"),

        @Index(name = "mulitIndexReleaseDateIdAsc", columnList = "releaseDate ASC, id"),
        @Index(name = "mulitIndexReleaseDateIdDesc", columnList = "releaseDate DESC, id"),

        @Index(name = "mulitIndexAvgScoreIdAsc", columnList = "avg_score ASC, id"),
        @Index(name = "mulitIndexAvgScoreIdDesc", columnList = "avg_score DESC, id"),

        @Index(name = "mulitIndexTotalRatingIdAsc", columnList = "total_rating ASC, id"),
        @Index(name = "mulitIndexTotalRatingIdDesc", columnList = "total_rating DESC, id"),
})
public class Game extends InteractiveEntity {

    private String name;

    @Column(length = 2000)
    @JsonProperty("summary")
    private String description;

    @Column(name = "`imageURL`")
    @JsonProperty("cover")
    private String imageURL;

    @Column(name = "releaseDate")
    @JsonProperty("first_release_date")
    private LocalDateTime releaseDate;

    @Column(name = "avg_score")
    private double avgScore;

    @Column(name = "total_rating")
    @JsonProperty("total_rating_count")
    private int totalRating;

    @Column(name = "`bannerURL`")
    @JsonProperty("screenshots")
    private String bannerURL;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "games_genres",
            joinColumns = @JoinColumn(
                    name = "game_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "genre_id", referencedColumnName = "id"
            ),
            indexes = {
                    @Index(name = "game_genres_gameId", columnList = "game_id"),
                    @Index(name = "game_genres_genreId", columnList = "genre_id")
            }
    )
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "games_platforms",
            joinColumns = @JoinColumn(
                    name = "game_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "platform_id", referencedColumnName = "id"
            ),
            indexes = {
                    @Index(name = "game_platforms_gameId", columnList = "game_id"),
                    @Index(name = "game_platforms_platformId", columnList = "platform_id")
            }
    )
    private Set<Platform> platforms = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(
            name = "games_tags",
            joinColumns = @JoinColumn(
                    name = "game_id", referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "tag_id", referencedColumnName = "id"
            ),
            indexes = {
                    @Index(name = "game_tags_gameId", columnList = "game_id"),
                    @Index(name = "game_tags_tagId", columnList = "tag_id")
            }
    )
    private Set<Tag> tags = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("game")
    @Column(name = "user_games")
    private Set<UserGame> userGames;

}
