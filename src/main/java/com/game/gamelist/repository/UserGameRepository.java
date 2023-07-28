package com.game.gamelist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Map;

public interface UserGameRepository extends JpaRepository<UserGameRepository, Long> {
    Map<?,?> findUserGameById(Long requestedId);
}
