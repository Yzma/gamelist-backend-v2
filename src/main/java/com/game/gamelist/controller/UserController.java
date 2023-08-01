package com.game.gamelist.controller;

import com.game.gamelist.entity.Game;
import com.game.gamelist.entity.User;
import com.game.gamelist.service.impl.GameServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final GameServiceImpl gameServiceImpl;

    @RequestMapping("/hello")
    public String helloWorld(@AuthenticationPrincipal User principal) {
        return "Hello World from user";
    }

    @RequestMapping("/games")
    public List<Game> getAllGames() {
        return gameServiceImpl.getAllGames();
    }
}
