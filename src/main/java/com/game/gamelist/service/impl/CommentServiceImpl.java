package com.game.gamelist.service.impl;

import com.game.gamelist.entity.Comment;
import com.game.gamelist.entity.InteractiveEntity;
import com.game.gamelist.entity.User;
import com.game.gamelist.exception.InvalidAuthorizationException;
import com.game.gamelist.exception.ResourceNotFoundException;
import com.game.gamelist.model.CommentView;
import com.game.gamelist.repository.CommentRepository;
import com.game.gamelist.repository.InteractiveEntityRepository;
import com.game.gamelist.repository.UserRepository;
import com.game.gamelist.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final InteractiveEntityRepository interactiveEntityRepository;

    @Override
    public CommentView createComment(User principle, Long interactiveEntityId, String text) {

        InteractiveEntity interactiveEntity = interactiveEntityRepository.findById(interactiveEntityId).orElseThrow(() -> new ResourceNotFoundException("Interactive entity not found"));

        User user = userRepository.findById(principle.getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Comment comment = Comment.builder().text(text).user(user).interactiveEntity(interactiveEntity).likes(new ArrayList<>()).comments(new ArrayList<>()).build();

        comment = commentRepository.save(comment);

        CommentView commentView = commentRepository.findProjectedById(comment.getId()).orElseThrow(() -> new ResourceNotFoundException("Comment not created successfully"));
        return commentView;
    }

    @Override
    public void deleteCommentById(User principle, Long commentId) {
        InteractiveEntity comment = commentRepository.findById(commentId).orElseThrow(() -> new ResourceNotFoundException("Comment was not found"));

        if(((Comment) comment).getUser().getId() != principle.getId()) {
            throw new InvalidAuthorizationException("You are not authorized to delete this comment");
        }

        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentView updateCommentById(User principle, Long interactiveEntityId, String text) {
        return null;
    }
}
