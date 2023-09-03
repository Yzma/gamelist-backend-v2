package com.game.gamelist.repository;

import com.game.gamelist.entity.UserGame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;


public interface UserGameRepository extends JpaRepository<UserGame, Long> {
    UserGame findFirstByUserIdAndGameId(Long userId, Long gameId);

    Optional<Set<UserGame>> findAllByUserId(Long userId);

    Optional<UserGame> findByGameIdAndUserId(Long gameId, Long userId);

    boolean existsByGameIdAndUserId(Long gameId, Long userId);
}
