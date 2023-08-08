package com.game.gamelist.controller;

import com.game.gamelist.entity.Game;
import com.game.gamelist.entity.User;
import com.game.gamelist.service.impl.GameServiceImpl;
import com.game.gamelist.service.impl.UserServiceImpl;
import com.game.gamelist.utils.JacksonValueUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    private final GameServiceImpl gameServiceImpl;
    private final UserServiceImpl userServiceImpl;

    @RequestMapping("/hello")
    public String helloWorld(@AuthenticationPrincipal User principal) {
        return "Hello World from user";
    }

    @RequestMapping("/games")
    public List<Game> getAllGames() {
        return gameServiceImpl.getAllGames();
    }

    @GetMapping("/userinfo")
    public ResponseEntity<MappingJacksonValue> getUser(@AuthenticationPrincipal User principal) {
        User user = userServiceImpl.getUser(principal.getId());
        return new ResponseEntity<>(JacksonValueUtil.getMappingJacksonValue(user), HttpStatus.OK);
    }
}
