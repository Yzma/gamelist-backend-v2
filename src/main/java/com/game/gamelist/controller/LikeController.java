package com.game.gamelist.controller;

import com.game.gamelist.entity.User;
import com.game.gamelist.model.CreateLikeEntityRequest;
import com.game.gamelist.model.HttpResponse;
import com.game.gamelist.projection.LikeEntityView;
import com.game.gamelist.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/likes")
public class LikeController {
    private final LikeService likeService;

    @PostMapping
    @Transactional
    public ResponseEntity<HttpResponse> createLike(@AuthenticationPrincipal User principal, @RequestBody CreateLikeEntityRequest createLikeEntityRequest) {
        LikeEntityView like = likeService.createLike(principal, createLikeEntityRequest.getInteractiveEntityId());

        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder().timeStamp(LocalDateTime.now().toString()).data(Map.of("like", like)).status(HttpStatus.CREATED).statusCode(HttpStatus.CREATED.value()).message("Like created successfully").build()
        );
    }

    @DeleteMapping("/{requestedId}")
    @Transactional
    public ResponseEntity<HttpResponse> deleteLike(@AuthenticationPrincipal User principal, @PathVariable Long requestedId) {
        likeService.deleteLikeById(principal, requestedId);

        return ResponseEntity.ok(
                HttpResponse.builder().timeStamp(LocalDateTime.now().toString()).status(HttpStatus.NO_CONTENT).statusCode(HttpStatus.NO_CONTENT.value()).message("Like deleted successfully").build()
        );
    }
}
