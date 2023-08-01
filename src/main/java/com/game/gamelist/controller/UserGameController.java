package com.game.gamelist.controller;


import com.game.gamelist.entity.Game;
import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.User;
import com.game.gamelist.entity.UserGame;
import com.game.gamelist.model.HttpResponse;
import com.game.gamelist.repository.GameRepository;
import com.game.gamelist.repository.PostRepository;
import com.game.gamelist.repository.UserGameRepository;
import com.game.gamelist.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.net.URI;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/usergames")
public class UserGameController {
    private final UserGameRepository userGameRepository;
    private final GameRepository gameRepository;

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

        Optional<UserGame> userGameOptional = userGameRepository.findById(requestedId);


        if (userGameOptional != null) {
            UserGame responseData = userGameOptional.get();

            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .timeStamp(LocalDateTime.now().toString())
                            .data(Map.of("userGame", responseData))
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .message("UserGame found")
                            .build());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<HttpResponse> createUserGame(@RequestBody UserGame userGame, @AuthenticationPrincipal User principal) {

        System.out.println("User Name: " + principal.getEmail());

        if (principal != null) {

            Game game = gameRepository.findById(userGame.getGame().getId()).orElse(null);

            System.out.println("Game ID is: " + game.getId());

            userGame.setUser(principal);
            userGame.setGame(game);

            userGameRepository.save(userGame);
            return ResponseEntity.created(URI.create("")).body(
                    HttpResponse.builder()
                            .timeStamp(LocalDateTime.now().toString())
                            .data(Map.of("userGame", userGame))
                            .status(HttpStatus.CREATED)
                            .statusCode(HttpStatus.CREATED.value())
                            .message("UserGame created")
                            .build());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
