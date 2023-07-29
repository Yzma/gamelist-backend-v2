package com.game.gamelist.repository;

import com.game.gamelist.entity.UserGame;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserGameRepository extends JpaRepository<UserGame, Long> {
}
