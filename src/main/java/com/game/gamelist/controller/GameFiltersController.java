package com.game.gamelist.controller;

import com.game.gamelist.model.HttpResponse;
import com.game.gamelist.service.impl.GameFilterServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/gamefilters")
@CrossOrigin(origins = "*")
public class GameFiltersController {

    private final GameFilterServiceImpl gameFilterService;

    @GetMapping
    public ResponseEntity<HttpResponse> getGameFilters() {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("gameFilters", gameFilterService.getGameFilters()))
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build()
        );
    }
}
