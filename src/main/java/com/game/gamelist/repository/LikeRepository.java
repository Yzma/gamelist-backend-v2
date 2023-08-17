package com.game.gamelist.repository;

import com.game.gamelist.entity.LikeEntity;
import com.game.gamelist.entity.LikeableEntity;
import com.game.gamelist.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

    @Query("SELECT l FROM like_entities l WHERE l.user = ?1 AND l.likeableEntity = ?2")
    LikeEntity findByUserAndLikeable(User principle, LikeableEntity likeableEntity);
}
