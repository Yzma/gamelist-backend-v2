package com.game.gamelist.repository;

import com.game.gamelist.entity.InteractiveEntity;
import com.game.gamelist.entity.StatusUpdate;
import com.game.gamelist.projection.PostView;
import com.game.gamelist.projection.StatusUpdateView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface InteractiveEntityRepository extends JpaRepository<InteractiveEntity, Long> {

        @Query("SELECT i FROM interactive_entities i WHERE " +
                "i.id IN (SELECT p.id FROM posts p WHERE p.user.id = :userId) OR " +
                "i.id IN (SELECT su.id FROM status_updates su JOIN su.userGame ug WHERE ug.user.id = :userId) " +
                "ORDER BY i.createdAt DESC")
        List<InteractiveEntity> findAllPostsAndStatusUpdatesByUserId(@Param("userId") Long userId);

        @Query("SELECT i FROM interactive_entities i WHERE " +
                "(i.id IN (SELECT p.id FROM posts p WHERE p.user.id = :userId) OR " +
                "i.id IN (SELECT su.id FROM status_updates su JOIN su.userGame ug WHERE ug.user.id = :userId)) " +
                "AND i.id < :id " +
                "ORDER BY i.createdAt DESC " +
                "LIMIT :limit")
        List<InteractiveEntity> findPostsAndStatusUpdatesByUserIdAndStartingWithIdDesc(
                @Param("userId") Long userId,
                @Param("id") Long id,
                @Param("limit") int limit
        );

        @Query("SELECT i FROM interactive_entities i WHERE " +
                "(i.id IN (SELECT p.id FROM posts p WHERE p.user.id = :userId) OR " +
                "i.id IN (SELECT su.id FROM status_updates su JOIN su.userGame ug WHERE ug.user.id = :userId)) " +
                "ORDER BY i.createdAt DESC "  +
                "LIMIT :limit")
        List<InteractiveEntity>findPostsAndStatusUpdatesByUserIdFirstPage(
                @Param("userId") Long userId,
                @Param("limit") int limit
        );

}
