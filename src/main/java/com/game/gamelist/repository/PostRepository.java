package com.game.gamelist.repository;

import com.game.gamelist.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Set<Post>> findAllByUserId(Long userId);


}
