package com.game.gamelist.repository;

import com.game.gamelist.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("SELECT EXTRACT(YEAR FROM MAX(releaseDate)) FROM games")
    int getFurthestYear();
}
