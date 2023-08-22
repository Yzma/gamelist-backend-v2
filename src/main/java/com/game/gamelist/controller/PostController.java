package com.game.gamelist.controller;


import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.User;
import com.game.gamelist.model.HttpResponse;
import com.game.gamelist.model.PostView;
import com.game.gamelist.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @GetMapping("/")
    public ResponseEntity<HttpResponse> getAllPostsByUser(@AuthenticationPrincipal User principal) {
        Set<PostView> posts = postService.findAllPostsByUserId(principal);

        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("posts", posts))
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Posts retrieved successfully")
                        .build()
        );
    }

    @GetMapping("/all")
    public ResponseEntity<HttpResponse> getAllPosts(@AuthenticationPrincipal User principal) {
        List<PostVie> posts = postService.findAllPosts(principal);

        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("posts", posts))
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Posts retrieved successfully")
                        .build()
        );
    }

    @GetMapping("/{requestedId}")
    public ResponseEntity<HttpResponse> findPostById(@PathVariable Long requestedId, @AuthenticationPrincipal User principal) {
        PostView post = postService.findPostById(requestedId, principal);

        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("post", post))
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Post retrieved successfully")
                        .build()
        );
    }

    @PostMapping("/")
    public ResponseEntity<HttpResponse> createPost(@RequestBody Post post, @AuthenticationPrincipal User principal) {
        Post createdPost = postService.createPost(post, principal);

        return ResponseEntity.created(URI.create(""))
                .body(
                        HttpResponse.builder()
                                .timeStamp(LocalDateTime.now().toString())
                                .data(Map.of("post", createdPost))
                                .status(HttpStatus.CREATED)
                                .statusCode(HttpStatus.CREATED.value())
                                .message("Post created successfully")
                                .build()
                );
    }

    @PutMapping("/{requestedId}")
    public ResponseEntity<HttpResponse> updatePost(@PathVariable Long requestedId, @RequestBody Post post, @AuthenticationPrincipal User principal) {
        PostView updatedPost = postService.updatePostById(requestedId, post, principal);

        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of("post", updatedPost))
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .message("Post updated successfully")
                        .build()
        );
    }

    @DeleteMapping("/{requestedId}")
    public ResponseEntity<HttpResponse> deletePostById(@PathVariable Long requestedId, @AuthenticationPrincipal User principal) {
        postService.deletePostById(requestedId, principal);

        return ResponseEntity.ok(
                HttpResponse.builder()
                        .timeStamp(LocalDateTime.now().toString())
                        .data(Map.of())
                        .status(HttpStatus.NO_CONTENT)
                        .statusCode(HttpStatus.NO_CONTENT.value())
                        .message("Post deleted successfully")
                        .build()
        );
    }

}