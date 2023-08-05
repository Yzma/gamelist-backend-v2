package com.game.gamelist.repository;

import com.game.gamelist.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, Long> {

    @Query("SELECT name FROM genres")
    List<String> getAllNames();
}
