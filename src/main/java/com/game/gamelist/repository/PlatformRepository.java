package com.game.gamelist.repository;

import com.game.gamelist.entity.Platform;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PlatformRepository extends JpaRepository<Platform, Long> {

    @Query("SELECT name FROM platforms")
    List<String> getAllNames();

    @Query("SELECT p FROM platforms p WHERE p.name = ?1")
    Platform findByName(String platformName);
}
