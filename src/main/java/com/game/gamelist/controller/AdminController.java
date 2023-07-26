package com.game.gamelist.controller;

import com.game.gamelist.entity.User;
import com.game.gamelist.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserServiceImpl userServiceImpl;

    @GetMapping("/hello")
    public String helloWorld() {
        return "Hello World from admin";
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userServiceImpl.getAllUsers();
    }
}
