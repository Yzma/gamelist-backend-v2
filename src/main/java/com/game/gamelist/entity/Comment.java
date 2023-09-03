package com.game.gamelist.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;
//@DiscriminatorValue("comment_type")

@Getter
@Setter
@SuperBuilder
@PrimaryKeyJoinColumn(name = "comment_id")
@NoArgsConstructor
@Entity(name = "comments")
public class Comment extends InteractiveEntity {
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interactive_entity_id", referencedColumnName = "id")
    private InteractiveEntity interactiveEntity;


}
