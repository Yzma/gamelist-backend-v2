package com.game.gamelist.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.game.gamelist.entity.GameJournal;

import java.util.Optional;

public interface GameJournalRepository extends JpaRepository<GameJournal, Long> {
}
