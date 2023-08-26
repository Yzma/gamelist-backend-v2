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

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@Entity(name = "status_updates")
@DiscriminatorValue("status_update_type")
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
}
