package com.game.gamelist.controller;

import com.game.gamelist.model.HttpResponse;
import com.game.gamelist.model.LoginRequest;
import com.game.gamelist.model.RegisterRequest;
import com.game.gamelist.service.impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173/"})
public class AuthController {
    private final AuthServiceImpl authService;

    @PostMapping("/login")
    public ResponseEntity<HttpResponse> login(@RequestBody @Validated LoginRequest requests) {

        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(authService.attemptLogin(requests.getEmail(), requests.getPassword()))
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Login successfully")
                        .build());
    }

    @PostMapping("/register")
    public ResponseEntity<HttpResponse> register(@RequestBody @Validated RegisterRequest requests) {

        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(authService.attemptSignup(requests.getEmail(), requests.getPassword(), requests.getUsername()))
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Register successfully")
                        .build());
    }
}
