package com.game.gamelist.repository;

import com.game.gamelist.entity.LikeEntity;
import com.game.gamelist.projection.LikeEntityView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

    Optional<LikeEntityView> findLikeEntityViewById(Long id);

    LikeEntityView findProjectedById(Long likeEntityId);

    boolean existsByUserIdAndInteractiveEntityId(Long userId, Long interactiveEntityId);

    Optional<LikeEntity> findByUserIdAndInteractiveEntityId(Long userId, Long interactiveEntityId);

    void deleteByUserIdAndInteractiveEntityId(Long userId, Long interactiveEntityId);
}
