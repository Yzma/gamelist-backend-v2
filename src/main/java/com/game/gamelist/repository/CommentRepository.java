package com.game.gamelist.repository;

import com.game.gamelist.entity.Comment;
import com.game.gamelist.projection.CommentView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByInteractiveEntityId(Long interactiveEntityId);

    List<CommentView> findProjectedByInteractiveEntityId(Long interactiveEntityId);

    Optional<CommentView> findProjectedById(Long id);
}
