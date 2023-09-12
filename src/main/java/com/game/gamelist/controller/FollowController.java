package com.game.gamelist.controller;

import com.game.gamelist.entity.User;
import com.game.gamelist.model.HttpResponse;
import com.game.gamelist.projection.FollowView;
import com.game.gamelist.projection.UserBasicView;
import com.game.gamelist.service.FollowService;
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
@RequestMapping("/api/v1/follows")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173/"})
public class FollowController {
    private final FollowService followService;

    @GetMapping
    @Transactional
    public ResponseEntity<HttpResponse> getAllFollows(@AuthenticationPrincipal User principal) {
        FollowView userFollows = followService.getAllFollows(principal);

        return ResponseEntity.ok(
                HttpResponse.builder().timeStamp(LocalDateTime.now().toString()).data(Map.of("user", userFollows)).status(HttpStatus.OK).statusCode(HttpStatus.OK.value()).message("Follows retrieved successfully").build()
        );
    }

    @PostMapping("/{userId}")
    @Transactional
    public ResponseEntity<HttpResponse> createFollow(@AuthenticationPrincipal User principal, @PathVariable Long userId) {
        UserBasicView userToFollow = followService.createFollow(principal, userId);

        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder().timeStamp(LocalDateTime.now().toString()).data(Map.of("user", userToFollow)).status(HttpStatus.CREATED).statusCode(HttpStatus.CREATED.value()).message("Follow created successfully").build()
        );
    }

    @DeleteMapping("/{requestedId}")
    @Transactional
    public ResponseEntity<HttpResponse> removeFollow(@AuthenticationPrincipal User principal, @PathVariable Long requestedId) {
        UserBasicView userToUnfollow = followService.removeFollow(principal, requestedId);

        return ResponseEntity.ok(
                HttpResponse.builder().timeStamp(LocalDateTime.now().toString()).data(Map.of("user", userToUnfollow)).status(HttpStatus.OK).statusCode(HttpStatus.OK.value()).message("Follow removed successfully").build()
        );
    }

    @PutMapping("/followers/{requestedId}")
    @Transactional
    public ResponseEntity<HttpResponse> removeFollower(@AuthenticationPrincipal User principal, @PathVariable Long requestedId) {
        UserBasicView userToUnfollow = followService.removeFollower(principal, requestedId);

        return ResponseEntity.ok(
                HttpResponse.builder().timeStamp(LocalDateTime.now().toString()).data(Map.of("user", userToUnfollow)).status(HttpStatus.OK).statusCode(HttpStatus.OK.value()).message("Follower removed successfully").build()
        );
    }
}
