package com.game.gamelist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

//@DiscriminatorValue("game_type")

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity(name = "games")
public class Game extends InteractiveEntity {

    private String name;

    @Column(length = 2000)
    @JsonProperty("summary")
    private String description;

    @Column(name = "`imageURL`")
    @JsonProperty("cover")
    private String imageURL;

    @Column(name = "`releaseDate`")
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
            )
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
            )
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
            )
    )
    private Set<Tag> tags = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("game")
    @Column(name = "user_games")
    private Set<UserGame> userGames;

}
