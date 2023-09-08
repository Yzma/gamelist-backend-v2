package com.game.gamelist.controller;


import com.game.gamelist.dto.UserGamesSummaryDTO;
import com.game.gamelist.entity.User;
import com.game.gamelist.entity.UserGame;
import com.game.gamelist.model.EditUserGameRequest;
import com.game.gamelist.model.HttpResponse;
import com.game.gamelist.service.UserGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/usergames")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173/"})
public class UserGameController {

    private final UserGameService userGameService;


    @GetMapping
    public ResponseEntity<HttpResponse> getAllUserGameByUserId(@AuthenticationPrincipal User principal) {
        Set<UserGame> userGames = userGameService.findAllUserGamesByUserId(principal);

        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("userGames", userGames))
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("UserGames retrieved successfully")
                        .build());
    }

    @GetMapping("/status")
    public ResponseEntity<HttpResponse> getAllUserGameByUserIdByStatus(@AuthenticationPrincipal User principal) {
        UserGamesSummaryDTO userGames = userGameService.findAllUserGamesByUserIdByStatus(principal);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("userGamesByStatus", userGames))
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("UserGames retrieved successfully")
                        .build());
    }

    @GetMapping("/{requestedId}")
    public ResponseEntity<HttpResponse> findUserGameByGameId(@PathVariable("requestedId") Long requestedId, @AuthenticationPrincipal User principal) {
        UserGame userGame = userGameService.findUserGameByGameId(requestedId, principal);

        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("userGame", userGame))
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("UserGame by Game ID found")
                        .build());

    }

    @PostMapping
    public ResponseEntity<HttpResponse> createUserGame(@RequestBody EditUserGameRequest userGame, @AuthenticationPrincipal User principal) {
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

    @PutMapping
    public ResponseEntity<HttpResponse> updateUserGame(@RequestBody EditUserGameRequest userGame, @AuthenticationPrincipal User principal) {
        UserGame updatedUserGame = userGameService.updateUserGameById(userGame, principal);

        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("userGame", updatedUserGame))
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("UserGame updated")
                        .build());


    }

    @DeleteMapping("/{requestedId}")
    public ResponseEntity<HttpResponse> deleteUserGameByGameId(@PathVariable("requestedId") Long requestedId, @AuthenticationPrincipal User principal) {
        UserGame deletedUserGame = userGameService.deleteUserGameByGameId(requestedId, principal);

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

