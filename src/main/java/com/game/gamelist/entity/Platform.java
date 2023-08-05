package com.game.gamelist.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "platforms")
public class Platform {

    @JsonIgnore
    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String name;

    @JsonIgnore
    @CreationTimestamp
    private LocalDateTime createdAt;

    @JsonIgnore
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "games_platforms",
            joinColumns = @JoinColumn(name = "platform_id"),
            inverseJoinColumns = @JoinColumn(name = "game_id"))
    private Set<Game> games;
}
