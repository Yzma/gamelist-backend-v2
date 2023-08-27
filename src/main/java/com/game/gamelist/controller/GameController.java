package com.game.gamelist.controller;

import com.game.gamelist.dto.GameDTO;
import com.game.gamelist.model.GameQueryFilters;
import com.game.gamelist.model.HttpResponse;
import com.game.gamelist.service.impl.GameServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/games")
@CrossOrigin(origins = "*")
public class GameController {

    private final GameServiceImpl gameService;

    @PostMapping
    public ResponseEntity<HttpResponse> getGames(@RequestBody(required = false) GameQueryFilters gameQueryFilters) {
        List<GameDTO> games = gameService.getAllGames(gameQueryFilters);
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("games", games))
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
}
