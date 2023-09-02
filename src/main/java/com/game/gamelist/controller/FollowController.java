package com.game.gamelist.controller;

import com.game.gamelist.entity.User;
import com.game.gamelist.model.HttpResponse;
import com.game.gamelist.projection.UserBasicView;
import com.game.gamelist.service.FollowService;
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
@RequestMapping("/api/v1/follows")
public class FollowController {
    private final FollowService followService;

    @PostMapping
    @Transactional
    public ResponseEntity<HttpResponse> createFollow(@AuthenticationPrincipal User principal, @RequestBody Long userId) {
        UserBasicView userToFollow = followService.createFollow(principal, userId);

        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder().timeStamp(LocalDateTime.now().toString()).data(Map.of("user", userToFollow)).status(HttpStatus.CREATED).statusCode(HttpStatus.CREATED.value()).message("Follow created successfully").build()
        );
    }


}
