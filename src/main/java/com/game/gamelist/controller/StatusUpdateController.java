package com.game.gamelist.controller;


import com.game.gamelist.entity.User;
import com.game.gamelist.model.HttpResponse;
import com.game.gamelist.projection.StatusUpdateView;
import com.game.gamelist.service.StatusUpdateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/statusupdates")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173/"})
public class StatusUpdateController {

    private final StatusUpdateService statusUpdateService;

    @GetMapping
    public ResponseEntity<HttpResponse> getAllStatusUpdatesByUser(@AuthenticationPrincipal User principal) {
        List<StatusUpdateView> statusUpdateViewList = statusUpdateService.findAllStatusUpdatesByUserId(principal);

        return ResponseEntity.ok(
            HttpResponse.builder()
                    .timeStamp(LocalDateTime.now().toString())
                    .data(Map.of("statusUpdates", statusUpdateViewList))
                    .status(HttpStatus.OK)
                    .statusCode(HttpStatus.OK.value())
                    .message("Status updates retrieved successfully")
                    .build()
        );
    }
}
