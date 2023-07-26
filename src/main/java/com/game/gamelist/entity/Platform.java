package com.game.gamelist.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity(name = "platforms")
@Table(indexes = {
        @Index(name = "name_index", columnList = "name"),
})
public class Platform {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
