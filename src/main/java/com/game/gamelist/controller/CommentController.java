package com.game.gamelist.controller;

import com.game.gamelist.entity.User;
import com.game.gamelist.model.CommentView;
import com.game.gamelist.model.CreateCommentRequest;
import com.game.gamelist.model.HttpResponse;
import com.game.gamelist.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    @Transactional
    public ResponseEntity<HttpResponse> createComment(@AuthenticationPrincipal User principal, @RequestBody CreateCommentRequest createCommentRequest) {
        CommentView comment = commentService.createComment(principal, createCommentRequest.getInteractiveEntityId(), createCommentRequest.getText());

        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder().timeStamp(LocalDateTime.now().toString()).data(Map.of("comment", comment)).status(HttpStatus.CREATED).statusCode(HttpStatus.CREATED.value()).message("Comment created successfully").build()
        );
    }
}
