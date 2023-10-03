package com.game.gamelist.repository;

import com.game.gamelist.entity.Post;
import com.game.gamelist.projection.PostView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<List<PostView>> findAllProjectedByUserId(Long userId);

    Optional<PostView> findProjectedById(Long postId);

    @Query("SELECT p FROM posts p LEFT JOIN FETCH p.likes l WHERE p.id = :postId")
    Optional<Post> findPostWithLikesById(@Param("postId") Long postId);

    @Query("SELECT p FROM posts p LEFT JOIN FETCH p.likes l")
    List<PostView> findAllPosts();
}
