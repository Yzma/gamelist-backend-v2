package com.game.gamelist.repository;

import com.game.gamelist.entity.LikeEntity;
import com.game.gamelist.model.LikeEntityView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

    Optional<LikeEntityView> findLikeEntityViewById(Long id);

    boolean existsByUserIdAndInteractiveEntityId(Long userId, Long interactiveEntityId);

//    @Query("SELECT l FROM like_entities l WHERE l.user = :user AND (l.post = :likeableEntity OR l.gameJournal = :likeableEntity)")
    Optional<LikeEntity> findByUserIdAndInteractiveEntityId(Long userId, Long interactiveEntityId);

    void deleteByUserIdAndInteractiveEntityId(Long userId, Long interactiveEntityId);
}
