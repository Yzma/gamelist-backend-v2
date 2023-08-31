package com.game.gamelist.repository;

import com.game.gamelist.entity.StatusUpdate;
import com.game.gamelist.model.StatusUpdateView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StatusUpdateRepository extends JpaRepository<StatusUpdate, Long> {

    @Query("SELECT s FROM status_updates s WHERE s.userGame.user.id = ?1")
    List<StatusUpdateView> findAllProjectedByUserId(Long userId);
}
