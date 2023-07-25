package com.game.gamelist.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    @RequestMapping("/hello")
    public String helloWorld() {
        return "Hello World from user";
    }
}
