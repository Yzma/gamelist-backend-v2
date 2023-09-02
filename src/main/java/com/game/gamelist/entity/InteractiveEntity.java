package com.game.gamelist.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//@DiscriminatorColumn(name = "entity_type", discriminatorType = DiscriminatorType.STRING)

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@Entity(name = "interactive_entities")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class InteractiveEntity {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "interactiveEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LikeEntity> likes = new ArrayList<>();

    @OneToMany(mappedBy = "interactiveEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    protected List<Comment> getComments() {
        return comments;
    }
    protected Long getId() {
        return id;
    }

     protected void setId(Long id) {
        this.id = id;
    }

    protected List<LikeEntity> getLikes() {
        return likes;
    }

    protected void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    protected LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    protected void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    protected LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
