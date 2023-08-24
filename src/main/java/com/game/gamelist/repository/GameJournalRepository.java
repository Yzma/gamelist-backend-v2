package com.game.gamelist.repository;

import com.game.gamelist.entity.GameJournal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameJournalRepository extends JpaRepository<GameJournal, Long> {
}
