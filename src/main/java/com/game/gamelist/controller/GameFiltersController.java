package com.game.gamelist.controller;

import com.game.gamelist.model.GameFilters;
import com.game.gamelist.service.impl.GameFilterServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/gamefilters")
public class GameFiltersController {

    private final GameFilterServiceImpl gameFilterService;

    @GetMapping
    public GameFilters getGameFilters() {
        return gameFilterService.getGameFilters();
    }
}
