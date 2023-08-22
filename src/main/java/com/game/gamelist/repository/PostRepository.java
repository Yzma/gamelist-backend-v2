package com.game.gamelist.repository;

import com.game.gamelist.entity.Post;
import com.game.gamelist.model.PostView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Set<Post>> findAllByUserId(Long userId);

    Optional<Set<PostView>> findAllProjectedByUserId(Long userId);

//    List<PostView> findAllProjected();

    Optional<PostView> findProjectedById(Long postId, Class<PostView> postViewClass);

    @Query("SELECT p FROM posts p LEFT JOIN FETCH p.likes l WHERE p.id = :postId")
    Optional<Post> findPostWithLikesById(@Param("postId") Long postId);

}
