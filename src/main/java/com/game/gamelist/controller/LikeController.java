package com.game.gamelist.controller;


import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.game.gamelist.entity.InteractiveEntity;
import com.game.gamelist.entity.LikeEntity;
import com.game.gamelist.entity.User;
import com.game.gamelist.model.HttpResponse;
import com.game.gamelist.service.LikeService;
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
@RequestMapping("/api/v1/likes")
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/")
    @Transactional
    public ResponseEntity<HttpResponse> createLike(@AuthenticationPrincipal User principal,@RequestBody Long interactiveEntityId
    ) {
        LikeEntity like = likeService.createLike(principal, interactiveEntityId);

        System.out.println("Liked User Email👹👹👹👹👹👹👹: " + like.getUser().getEmail());

        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder().timeStamp(LocalDateTime.now().toString()).data(Map.of("like", like)).status(HttpStatus.CREATED).statusCode(HttpStatus.CREATED.value()).message("Like created successfully").build()
        );
    }
}
