package com.game.gamelist.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@Entity(name = "game_journals")
@DiscriminatorValue("game_journal_type")
public class GameJournal extends InteractiveEntity {

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("game_journals")
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

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
}
