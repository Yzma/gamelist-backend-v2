package com.game.gamelist.repository;

import com.game.gamelist.entity.InteractiveEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InteractiveEntityRepository extends JpaRepository<InteractiveEntity, Long> {
}
