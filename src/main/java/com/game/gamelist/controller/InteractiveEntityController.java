package com.game.gamelist.controller;


import com.game.gamelist.entity.User;
import com.game.gamelist.model.HttpResponse;
import com.game.gamelist.model.PostAndStatusUpdateResponse;
import com.game.gamelist.service.InteractiveEntityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/interactive-entities")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173/"})
public class InteractiveEntityController {
    private final InteractiveEntityService interactiveEntityService;

    @GetMapping("/forum-pageable")
    @Transactional
    public ResponseEntity<HttpResponse> getAllPostAndStatusUpdatePageable(@RequestParam(value = "startingId", required = false) Long startingId, @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit) {
        PostAndStatusUpdateResponse postAndStatusUpdateResponse;

        if (startingId == 0) {
            postAndStatusUpdateResponse = interactiveEntityService.getAllPostAndStatusUpdatesFirstPage(limit);
        } else {
            postAndStatusUpdateResponse = interactiveEntityService.getAllPostAndStatusUpdatesByStartingId(startingId, limit);
        }

        return ResponseEntity.ok(
                HttpResponse.builder().timeStamp(LocalDateTime.now().toString()).data(Map.of("postsAndStatusUpdates", postAndStatusUpdateResponse)).status(HttpStatus.OK).statusCode(HttpStatus.OK.value()).message("Posts And StatusUpdates retrieved successfully. ").build()
        );
    }

    @GetMapping("/user-social/pageable")
    @Transactional
    public ResponseEntity<HttpResponse> getPostAndStatusUpdateByUserIdPageable(@AuthenticationPrincipal User principle, @RequestParam(value = "startingId", required = false) Long startingId, @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit) {
        PostAndStatusUpdateResponse postAndStatusUpdateResponse;

        if(startingId == 0)
            postAndStatusUpdateResponse = interactiveEntityService.getPostAndStatusUpdateByUserIdFirstPage(principle, limit);
        else
         postAndStatusUpdateResponse = interactiveEntityService.getPostAndStatusUpdateByUserIdAndStartingId(principle, startingId, limit);

        return ResponseEntity.ok(
                HttpResponse.builder().timeStamp(LocalDateTime.now().toString()).data(Map.of("postsAndStatusUpdates", postAndStatusUpdateResponse)).status(HttpStatus.OK).statusCode(HttpStatus.OK.value()).message("Posts And StatusUpdates retrieved successfully. ").build()
        );
    }


    @GetMapping("/user-social")
    @Transactional
    public ResponseEntity<HttpResponse> getPostAndStatusUpdateByUserId(@AuthenticationPrincipal User principle) {

        PostAndStatusUpdateResponse postAndStatusUpdateResponse = interactiveEntityService.getPostAndStatusUpdateByUserId(principle);

        return ResponseEntity.ok(
                HttpResponse.builder().timeStamp(LocalDateTime.now().toString()).data(Map.of("postsAndStatusUpdates", postAndStatusUpdateResponse)).status(HttpStatus.OK).statusCode(HttpStatus.OK.value()).message("Posts And StatusUpdates retrieved successfully. ").build()
        );
    }


}
