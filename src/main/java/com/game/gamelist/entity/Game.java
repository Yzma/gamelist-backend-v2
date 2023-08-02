package com.game.gamelist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity(name = "games")
public class Game {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String description;

    @Column(name = "`imageURL`")
    private String imageURL;

    @Column(name = "`releaseDate`")
    private LocalDateTime releaseDate;

    @Column(name = "avg_score")
    private double avgScore;

    @Column(name = "total_rating")
    private int totalRating;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "`bannerURL`")
    private String bannerURL;

    @ManyToMany(mappedBy = "games")
    @JsonIgnoreProperties("games")
    private Set<Genre> genres;

    @ManyToMany(mappedBy = "games")
    @JsonIgnoreProperties("games")
    private Set<Platform> platforms;

    @ManyToMany(mappedBy = "games")
    @JsonIgnoreProperties("games")
    private Set<Tag> tags;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("game")
    @JsonManagedReference
    @Column(name = "user_games")
    private Set<UserGame> userGames;


}
