package com.game.gamelist.controller;


import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.User;
import com.game.gamelist.entity.UserGame;
import com.game.gamelist.model.HttpResponse;
import com.game.gamelist.repository.PostRepository;
import com.game.gamelist.repository.UserGameRepository;
import com.game.gamelist.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/usergames")
public class UserGameController {
    private final UserGameRepository userGameRepository;
    private final PostRepository postRepository;

    @GetMapping("/")
    public ResponseEntity<HttpResponse> sayHello() {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("message", "Hello World"))
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Hello World")
                        .build());
    }


    @GetMapping("/{requestedId}")
    public ResponseEntity<HttpResponse> findUserGameById(@PathVariable("requestedId") Long requestedId) {
        UserGame userGame = userGameRepository.findById(requestedId).orElse(null);
        System.out.println("UserGame Found: " + userGame);

        Post post = postRepository.findById((long) requestedId).orElse(null);
        System.out.println("Post Found: " + post.getText());
        System.out.println("Post Found: " + post);
        if (userGame != null) {
            System.out.println("UserGame Found: " + userGame);
            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .timeStamp(LocalDateTime.now().toString())
                            .data(Map.of("Game", 1) )
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .message("UserGame found")
                            .build());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
