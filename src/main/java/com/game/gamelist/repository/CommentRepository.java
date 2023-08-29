package com.game.gamelist.repository;

import com.game.gamelist.entity.Comment;
import com.game.gamelist.model.CommentView;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByInteractiveEntityId(Long interactiveEntityId);

    List<CommentView> findProjectedByInteractiveEntityId(Long interactiveEntityId);
}
