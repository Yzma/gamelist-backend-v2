package com.game.gamelist.repository;

import com.game.gamelist.entity.UserGame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;


public interface UserGameRepository extends JpaRepository<UserGame, Long> {
    UserGame findFirstByUserIdAndGameId(Long userId, Long gameId);

    Optional<Set<UserGame>> findAllByUserId(Long userId);

    Optional<UserGame> findByGameIdAndUserId(Long gameId, Long userId);

    boolean existsByGameIdAndUserId(Long gameId, Long userId);
    @Query("SELECT CASE WHEN COUNT(ug) > 0 THEN true ELSE false END FROM user_games ug " +
            "WHERE ug.game.id = :gameId " +
            "AND ug.user.id = :userId " +
            "AND ug.gameStatus != 'Inactive'")
    boolean existsByGameIdAndUserIdAndGameStatusNotInactive(
            @Param("gameId") Long gameId,
            @Param("userId") Long userId
    );
}
