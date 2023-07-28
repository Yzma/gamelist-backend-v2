package com.game.gamelist.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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
    private Set<Genre> genres;

    @ManyToMany(mappedBy = "games")
    private Set<Platform> platforms;

    @ManyToMany(mappedBy = "games")
    private Set<Tag> tags;
}
