package com.game.gamelist.controller;


import com.fasterxml.jackson.annotation.JsonView;
import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.User;
import com.game.gamelist.entity.Views;
import com.game.gamelist.model.HttpResponse;
import com.game.gamelist.service.PostService;
import com.game.gamelist.utils.JacksonValueUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashMap;
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
        Set<Post> posts = postService.findAllPostsByUserId(principal);

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
        List<Post> posts = postService.findAllPosts(principal);

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

//    @GetMapping("/{requestedId}")
//    @JsonView(Views.InteractiveView.class)
//    public ResponseEntity<HttpResponse> findPostById(@PathVariable Long requestedId, @AuthenticationPrincipal User principal) {
//        Post post = postService.findPostById(requestedId, principal);
//
//        System.out.println(post.getText());
//
//        return ResponseEntity.ok(
//                HttpResponse.builder()
//                        .timeStamp(LocalDateTime.now().toString())
//                        .data(Map.of("post", post))
//                        .status(HttpStatus.OK)
//                        .statusCode(HttpStatus.OK.value())
//                        .message("Post retrieved successfully")
//                        .build()
//        );
//    }

    @GetMapping("/{requestedId}")
    @JsonView(Views.InteractiveView.class)
    public ResponseEntity<Object> findPostById(@PathVariable Long requestedId, @AuthenticationPrincipal User principal, @RequestParam(required = false, defaultValue = "false") boolean applyFilter
    ) {
        Post post = postService.findPostById(requestedId, principal);

        System.out.println("??👹👹👹👹👹👹👹User Id: " + post.getLikes().get(1).getUser().getBio());

        MappingJacksonValue mappingJacksonValue = JacksonValueUtil.getMappingJacksonValue(post.getLikes().get(1).getUser(), applyFilter);

//        System.out.println("??👹👹👹👹👹👹👹User Id: " + ((Post)mappingJacksonValue.getValue()).getLikes().get(1).getUser().getBio());


        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("timeStamp", LocalDateTime.now().toString());
        responseBody.put("statusCode", HttpStatus.OK.value());
        responseBody.put("status", HttpStatus.OK);
        responseBody.put("message", "Post retrieved successfully");
        responseBody.put("data", mappingJacksonValue);

        return ResponseEntity.ok(responseBody);
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
        Post updatedPost = postService.updatePostById(requestedId, post, principal);

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