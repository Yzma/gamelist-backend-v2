package com.game.gamelist.repository;

import com.game.gamelist.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Long> {

    @Query("SELECT name FROM tags")
    List<String> getAllNames();
}
