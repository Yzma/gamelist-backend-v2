package com.game.gamelist.repository;

import com.game.gamelist.entity.LikeEntity;
import com.game.gamelist.model.LikeEntityView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

    Optional<LikeEntityView> findLikeEntityViewById(Long id);

    LikeEntityView findProjectedById(Long likeEntityId, Class<LikeEntityView> likeEntityViewClass);

    boolean existsByUserIdAndInteractiveEntityId(Long userId, Long interactiveEntityId);

    Optional<LikeEntity> findByUserIdAndInteractiveEntityId(Long userId, Long interactiveEntityId);

    void deleteByUserIdAndInteractiveEntityId(Long userId, Long interactiveEntityId);
}
