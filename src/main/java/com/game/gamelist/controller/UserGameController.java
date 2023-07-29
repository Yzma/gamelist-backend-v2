package com.game.gamelist.controller;


import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.User;
import com.game.gamelist.entity.UserGame;
import com.game.gamelist.model.HttpResponse;
import com.game.gamelist.repository.PostRepository;
import com.game.gamelist.repository.UserGameRepository;
import com.game.gamelist.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Field;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/usergames")
public class UserGameController {
    private final UserGameRepository userGameRepository;
    private final PostRepository postRepository;

    @GetMapping("/")
    public ResponseEntity<HttpResponse> sayHello() {
        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("message", "Hello World"))
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Hello World")
                        .build());
    }


    @GetMapping("/{requestedId}")
    public ResponseEntity<HttpResponse> findUserGameById(@PathVariable("requestedId") Long requestedId) {
        UserGame userGame = userGameRepository.findById(requestedId).orElse(null);

        if (userGame != null) {
            Map<String, Object> responseData = new HashMap<>();
            Field[] fields = userGame.getClass().getDeclaredFields();

            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    if (field.getName().equals("user") || field.getName().equals("game")) {
                        continue;
                    }
                    responseData.put(field.getName(), field.get(userGame));
                } catch (IllegalAccessException e) {
                    // Handle the exception if necessary
                }
            }
            return ResponseEntity.ok(
                    HttpResponse.builder()
                            .timeStamp(LocalDateTime.now().toString())
                            .data(Map.of("usergame", responseData))
                            .status(HttpStatus.OK)
                            .statusCode(HttpStatus.OK.value())
                            .message("UserGame found")
                            .build());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
