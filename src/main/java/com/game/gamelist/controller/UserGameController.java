package com.game.gamelist.controller;


import com.game.gamelist.entity.Game;
import com.game.gamelist.entity.Genre;
import com.game.gamelist.entity.User;
import com.game.gamelist.entity.UserGame;
import com.game.gamelist.model.HttpResponse;
import com.game.gamelist.repository.GameRepository;
import com.game.gamelist.repository.UserGameRepository;
import com.game.gamelist.service.UserGameService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/usergames")
public class UserGameController {
    private final UserGameRepository userGameRepository;
    private final GameRepository gameRepository;
    private final UserGameService userGameService;


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

        Optional<UserGame> userGameOptional = userGameService.findUserGameById(requestedId);


        if (userGameOptional.isPresent()) {

            UserGame responseData = userGameOptional.get();
            Game game = responseData.getGame();

            Set<Genre> genres = game.getGenres();

            Set<UserGame> userGamesFromGame = game.getUserGames();

            for (UserGame userGame : userGamesFromGame) {
                System.out.println(userGame.getGameStatus());
            }

            for (Genre genre : genres) {
                System.out.println("Genre: " + genre.getName());
            }

            System.out.println("");
            System.out.println("Game found: " + responseData.getGame().getName());
            System.out.println("Owner found: " + responseData.getUser().getEmail());
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

        System.out.println("IS PRINCIPAL NULL? " + principal);
        if (principal != null) {

            UserGame createdUserGame = userGameService.createUserGame(userGame, principal);

            if (createdUserGame != null) {
                return ResponseEntity.created(URI.create("")).body(
                        HttpResponse.builder()
                                .timeStamp(LocalDateTime.now().toString())
                                .data(Map.of("userGame", createdUserGame))
                                .status(HttpStatus.CREATED)
                                .statusCode(HttpStatus.CREATED.value())
                                .message("UserGame created")
                                .build());
            } else {
                // Handle the case when the game is not found or other errors
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        HttpResponse.builder()
                                .timeStamp(LocalDateTime.now().toString())
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .message("Error creating UserGame")
                                .build());
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
