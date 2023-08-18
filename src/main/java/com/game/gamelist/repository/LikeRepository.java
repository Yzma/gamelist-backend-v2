package com.game.gamelist.repository;

import com.game.gamelist.entity.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeEntity, Long> {

//    @Query("SELECT l FROM like_entities l WHERE l.user = :user AND (l.post = :likeableEntity OR l.gameJournal = :likeableEntity)")
//    LikeEntity findByUserAndLikeable(User user, LikeableEntity likeableEntity);
}
