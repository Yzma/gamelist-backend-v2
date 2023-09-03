package com.game.gamelist.repository;

import com.game.gamelist.entity.Game;
import com.game.gamelist.entity.GameStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Long>, JpaSpecificationExecutor<Game> {

    @Query("SELECT EXTRACT(YEAR FROM MAX(releaseDate)) FROM games")
    int getFurthestYear();

    @Query("SELECT g FROM games g ORDER BY g.id ASC")
    List<Game> findAllGamesOrderedById();

    @Query("SELECT g FROM user_games ug JOIN ug.game g WHERE ug.user.id = :userId AND ug.gameStatus = :status")
    List<Game> findGamesByUserIdAndStatus(@Param("userId") Long userId, @Param("status") GameStatus status);
    
}
