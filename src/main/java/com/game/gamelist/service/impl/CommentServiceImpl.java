package com.game.gamelist.service.impl;

import com.game.gamelist.entity.User;
import com.game.gamelist.model.CommentView;
import com.game.gamelist.repository.CommentRepository;
import com.game.gamelist.repository.InteractiveEntityRepository;
import com.game.gamelist.repository.UserRepository;
import com.game.gamelist.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final InteractiveEntityRepository interactiveEntityRepository;

    @Override
    public CommentView createComment(User principle, Long interactiveEntityId, String text) {
        return null;
    }

    @Override
    public void deleteCommentById(User principle, Long interactiveEntityId) {

    }

    @Override
    public CommentView updateCommentById(User principle, Long interactiveEntityId, String text) {
        return null;
    }
}
