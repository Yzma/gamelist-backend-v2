package com.game.gamelist.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

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

    @Column(name = "imageURL")
    private String imageURL;

    @Column(name = "releaseDate")
    private LocalDateTime releaseDate;

    private double avgScore;

    private int totalRating;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Column(name = "bannerURL")
    private String bannerURL;
}
