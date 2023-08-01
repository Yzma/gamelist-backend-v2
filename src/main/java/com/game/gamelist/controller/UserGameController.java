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
    public ResponseEntity<HttpResponse> findUserGameById(@PathVariable("requestedId") Long requestedId, @AuthenticationPrincipal User principal) {

        Optional<UserGame> userGameOptional = userGameService.findUserGameById(requestedId, principal);

        System.out.println("Logged in user: " + principal.getEmail());


        if (userGameOptional.isPresent()) {

            UserGame responseData = userGameOptional.get();

            System.out.println("The UserGame Belows to email: " + responseData.getUser().getEmail());

            Game game = responseData.getGame();

            Set<Genre> genres = game.getGenres();

            Set<UserGame> userGamesFromGame = game.getUserGames();

            for (UserGame userGame : userGamesFromGame) {
                System.out.println(userGame.getGameStatus());
            }

            for (Genre genre : genres) {
                System.out.println("Genre: " + genre.getName());
            }

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
//          When the userGame can not be created, return an error message 500
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

    @PutMapping("/{requestedId}")
    public ResponseEntity<HttpResponse> updateUserGame(@PathVariable("requestedId") Long requestedId, @RequestBody UserGame userGame, @AuthenticationPrincipal User principal) {

            System.out.println("User Name: " + principal.getEmail());

            System.out.println("IS PRINCIPAL NULL? " + principal);
            if (principal != null) {

                Optional<UserGame> updatedUserGameOptional = userGameService.updateUserGameById(requestedId, userGame, principal);

                if (updatedUserGameOptional.isPresent()) {

                    UserGame updatedUserGame = updatedUserGameOptional.get();

                    return ResponseEntity.created(URI.create("")).body(
                            HttpResponse.builder()
                                    .timeStamp(LocalDateTime.now().toString())
                                    .data(Map.of("userGame", updatedUserGame))
                                    .status(HttpStatus.NO_CONTENT)
                                    .statusCode(HttpStatus.NO_CONTENT.value())
                                    .message("UserGame updated")
                                    .build());
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                            HttpResponse.builder()
                                    .timeStamp(LocalDateTime.now().toString())
                                    .status(HttpStatus.NOT_FOUND)
                                    .statusCode(HttpStatus.NOT_FOUND.value())
                                    .message("Error updating UserGame")
                                    .build());
                }
            } else {
                return ResponseEntity.notFound().build();
            }
    }

    @DeleteMapping("/{requestedId}")
    public ResponseEntity<HttpResponse> deleteUserGameById(@PathVariable("requestedId") Long requestedId, @AuthenticationPrincipal User principal) {
        System.out.println("User Name: " + principal.getEmail());

        System.out.println("IS PRINCIPAL NULL? " + principal);
        if (principal != null) {

            Optional<UserGame> deletedUserGameOptional = userGameService.deleteUserGameById(requestedId, principal);

            if (deletedUserGameOptional.isPresent()) {

                UserGame deletedUserGame = deletedUserGameOptional.get();

                return ResponseEntity.created(URI.create("")).body(
                        HttpResponse.builder()
                                .timeStamp(LocalDateTime.now().toString())
                                .data(Map.of("userGame", deletedUserGame))
                                .status(HttpStatus.NO_CONTENT)
                                .statusCode(HttpStatus.NO_CONTENT.value())
                                .message("UserGame deleted")
                                .build());
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        HttpResponse.builder()
                                .timeStamp(LocalDateTime.now().toString())
                                .status(HttpStatus.NOT_FOUND)
                                .statusCode(HttpStatus.NOT_FOUND.value())
                                .message("Error deleting UserGame")
                                .build());
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
