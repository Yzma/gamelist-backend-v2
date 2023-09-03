package com.game.gamelist.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

//@DiscriminatorValue("post_type")

@Getter
@Setter
@SuperBuilder
@PrimaryKeyJoinColumn(name = "post_id")
@NoArgsConstructor
@Entity(name = "posts")
public class Post extends InteractiveEntity {

    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    public List<Comment> getComments() {
        return super.getComments();
    }
    public List<LikeEntity> getLikes() {
        return super.getLikes();
    }
}