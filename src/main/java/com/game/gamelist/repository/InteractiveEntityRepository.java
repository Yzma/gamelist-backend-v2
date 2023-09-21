package com.game.gamelist.repository;

import com.game.gamelist.entity.InteractiveEntity;
import com.game.gamelist.projection.PostView;
import com.game.gamelist.projection.StatusUpdateView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InteractiveEntityRepository extends JpaRepository<InteractiveEntity, Long> {

        @Query("SELECT i FROM interactive_entities i WHERE " +
                "i.id IN (SELECT p.id FROM posts p WHERE p.user.id = :userId) OR " +
                "i.id IN (SELECT su.id FROM status_updates su JOIN su.userGame ug WHERE ug.user.id = :userId) " +
                "ORDER BY i.createdAt DESC")
        List<InteractiveEntity> findAllPostsAndStatusUpdatesByUserId(Long userId);
}

//
// "UNION " +
//         "SELECT i.* " +
//         "FROM interactive_entities i " +
//         "JOIN status_updates su ON i.id = su.status_update_id " +
//         "JOIN user_games ug ON su.user_game_id = ug.id " +
//         "WHERE ug.user_id = :userId " +