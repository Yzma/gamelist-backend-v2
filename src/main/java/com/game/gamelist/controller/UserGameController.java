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
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/usergames")
public class UserGameController {

    private final UserGameService userGameService;


    @GetMapping("/")
    public ResponseEntity<HttpResponse> getAllUserGameByUserId(@AuthenticationPrincipal User principal) {

        Set<UserGame> userGames = userGameService.findAllUserGamesByUserId(principal);

        System.out.println("Logged in user: " + principal.getEmail());


        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("userGames", userGames))
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("UserGames retrieved successfully")
                        .build());
    }

    @GetMapping("/{requestedId}")
    public ResponseEntity<HttpResponse> findUserGameById(@PathVariable("requestedId") Long requestedId, @AuthenticationPrincipal User principal) {

        UserGame userGame = userGameService.findUserGameById(requestedId, principal);

        System.out.println("Logged in user: " + principal.getEmail());


        System.out.println("The UserGame Belows to email: " + userGame.getUser().getEmail());

        Game game = userGame.getGame();

        Set<Genre> genres = game.getGenres();

        Set<UserGame> userGamesFromGame = game.getUserGames();

        for (UserGame userGameInEachGame : userGamesFromGame) {
            System.out.println(userGameInEachGame.getGameStatus());
        }

        for (Genre genre : genres) {
            System.out.println("Genre: " + genre.getName());
        }

        System.out.println("Game found: " + userGame.getGame().getName());
        System.out.println("Owner found: " + userGame.getUser().getEmail());
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("userGame", userGame))
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("UserGame found")
                        .build());

    }

    @PostMapping("/")
    public ResponseEntity<HttpResponse> createUserGame(@RequestBody UserGame userGame, @AuthenticationPrincipal User principal) {

        System.out.println("User Name: " + principal.getEmail());

        System.out.println("IS PRINCIPAL NULL? " + principal);

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
//          When the userGame can not be created, return an error message 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    HttpResponse.builder()
                            .timeStamp(LocalDateTime.now().toString())
                            .status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .message("Error creating UserGame")
                            .build());
        }
    }

    @PutMapping("/{requestedId}")
    public ResponseEntity<HttpResponse> updateUserGame(@PathVariable("requestedId") Long requestedId, @RequestBody UserGame userGame, @AuthenticationPrincipal User principal) {

        System.out.println("User Name: " + principal.getEmail());

        System.out.println("IS PRINCIPAL NULL? " + principal);

        UserGame updatedUserGame = userGameService.updateUserGameById(requestedId, userGame, principal);


        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("userGame", updatedUserGame))
                        .status(HttpStatus.NO_CONTENT)
                        .statusCode(HttpStatus.NO_CONTENT.value())
                        .message("UserGame updated")
                        .build());


    }

    @DeleteMapping("/{requestedId}")
    public ResponseEntity<HttpResponse> deleteUserGameById(@PathVariable("requestedId") Long requestedId, @AuthenticationPrincipal User principal) {
        System.out.println("User Name: " + principal.getEmail());

        System.out.println("IS PRINCIPAL NULL? " + principal);


        UserGame deletedUserGame = userGameService.deleteUserGameById(requestedId, principal);


        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("userGame", deletedUserGame))
                        .status(HttpStatus.NO_CONTENT)
                        .statusCode(HttpStatus.NO_CONTENT.value())
                        .message("UserGame deleted")
                        .build());

    }
}

