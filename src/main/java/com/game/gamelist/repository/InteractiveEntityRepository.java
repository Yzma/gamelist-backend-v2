package com.game.gamelist.repository;

import com.game.gamelist.entity.InteractiveEntity;
import com.game.gamelist.projection.PostView;
import com.game.gamelist.projection.StatusUpdateView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InteractiveEntityRepository extends JpaRepository<InteractiveEntity, Long> {

        @Query(value = "SELECT ie.* " +
                "FROM interactive_entities ie " +
                "JOIN posts p ON ie.id = p.post_id " +
                "JOIN status_updates su ON ie.id = su.status_update_id " +
                "WHERE (p.user_id = :userId OR su.user_id = :userId)" +
                "ORDER BY ie.created_at DESC", nativeQuery = true)
        List<InteractiveEntity> findAllPostsAndStatusUpdatesByUserId(Long userId);
}
