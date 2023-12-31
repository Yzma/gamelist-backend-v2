package com.game.gamelist.controller;

import com.game.gamelist.entity.User;
import com.game.gamelist.model.HttpResponse;
import com.game.gamelist.service.impl.GameServiceImpl;
import com.game.gamelist.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173/"})
public class UserController {
    private final UserServiceImpl userServiceImpl;

    @GetMapping("/userinfo")
    public ResponseEntity<HttpResponse> getUser(@AuthenticationPrincipal User principal) {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(userServiceImpl.getUser(principal.getId()))
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Retrieved user successfully")
                        .build());
    }
}
