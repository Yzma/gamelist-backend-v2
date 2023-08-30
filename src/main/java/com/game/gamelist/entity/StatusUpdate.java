package com.game.gamelist.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

//@DiscriminatorValue("status_update_type")

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity(name = "status_updates")
@PrimaryKeyJoinColumn(name = "status_update_id")
public class StatusUpdate extends InteractiveEntity {

    @Column(name = "game_status")
    @Enumerated(EnumType.STRING)
    private GameStatus gameStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_game_id", referencedColumnName = "id")
    private UserGame userGame;
    public Long getId() {
        return super.getId();
    }

    public void setId(Long id) {
        super.setId(id);
    }

    public List<LikeEntity> getLikes() {
        return super.getLikes();
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
    public List<Comment> getComments() {
        return super.getComments();
    }
}
