package com.game.gamelist.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity(name = "comments")
@DiscriminatorValue("comment_type")
public class Comment extends InteractiveEntity {
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "interactive_entity_id", referencedColumnName = "id")
    private InteractiveEntity interactiveEntity;

    public Long getId() {
        return super.getId();
    }

    public void setId(Long id) {
        super.setId(id);
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        super.setUpdatedAt(updatedAt);
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        super.setCreatedAt(createdAt);
    }
    public LocalDateTime getCreatedAt() {
        return super.getCreatedAt();
    }
    public LocalDateTime getUpdatedAt() {
        return super.getUpdatedAt();
    }

    public List<LikeEntity> getLikes() {
        return super.getLikes();
    }
}
