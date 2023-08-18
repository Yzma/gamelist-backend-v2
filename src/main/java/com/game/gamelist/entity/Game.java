package com.game.gamelist.entity;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "games")
public class Game {
    @Id
    @GeneratedValue
    private Long id;

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

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "`bannerURL`")
    @JsonProperty("screenshots")
    private String bannerURL;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "games_genres",
            joinColumns = {@JoinColumn(name = "game_id")},
            inverseJoinColumns = {@JoinColumn(name = "genre_id")}
    )
    private Set<Genre> genres = new HashSet<>();


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "games_platforms",
            joinColumns = {@JoinColumn(name = "game_id")},
            inverseJoinColumns = {@JoinColumn(name = "platform_id")}
    )
    private Set<Platform> platforms = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "games_tags",
            joinColumns = {@JoinColumn(name = "game_id")},
            inverseJoinColumns = {@JoinColumn(name = "tag_id")}
    )
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    @Column(name = "user_games")
    private Set<UserGame> userGames;
}
