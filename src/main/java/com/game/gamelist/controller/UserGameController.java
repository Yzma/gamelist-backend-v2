package com.game.gamelist.controller;


import com.game.gamelist.model.HttpResponse;
import com.game.gamelist.repository.UserGameRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/usergames")
public class UserGameController {
    private final UserGameRepository userGameRepository;

    @GetMapping("/{requestedId}")
    public ResponseEntity<HttpResponse> findUserGameById(@PathVariable("requestedId") Long requestedId) {
        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(userGameRepository.findUserGameById(requestedId))
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("UserGame found")
                        .build());
    }

}
