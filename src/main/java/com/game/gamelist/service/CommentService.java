package com.game.gamelist.service;

import com.game.gamelist.entity.User;
import com.game.gamelist.projection.CommentView;

public interface CommentService {

    CommentView createComment(User principle, Long interactiveEntityId, String text);

    void deleteCommentById(User principle, Long commentId);

    CommentView updateCommentById(User principle, Long interactiveEntityId, String text);
}
