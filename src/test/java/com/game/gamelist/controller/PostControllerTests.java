package com.game.gamelist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.gamelist.config.SecurityTestConfig;
import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.Role;
import com.game.gamelist.entity.User;
import com.game.gamelist.service.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Set;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityTestConfig.class)
public class PostControllerTests {

    @InjectMocks
    private PostController postController;

    @MockBean
    private PostService postService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SecurityTestConfig.Auth0JwtTestUtils auth0JwtTestUtils;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(postController);
    }
    @Test
    void shouldReturnNewPostWhenPostIsCreated() throws Exception {
        User principal = User.builder().id(999L).username("testuser").password("testuser").email("testuser@gmail.com").userPicture("testuser").bio("testuser").bannerPicture("testuser").isActive(true).roles(Set.of(Role.ROLE_USER)).build();

        auth0JwtTestUtils.mockAuthentication(principal);

        Post mockPost = Post.builder().id(66L).text("Hello World!").user(principal).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        System.out.println("👹👹👹👹👹👹👹👹👹👹PostControllerTests: shouldReturnNewPostWhenPostIsCreated: mockPost Post Text: " + mockPost.getText());

        System.out.println("??👹👹👹👹👹👹👹👹👹PostControllerTests: shouldReturnNewPostWhenPostIsCreated: mockPost Post User: " + mockPost.getUser().getUsername());

        when(postService.createPost(Mockito.any(Post.class), Mockito.any(User.class))).thenReturn(mockPost);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/posts/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockPost)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.post.id").value(66L))
                .andExpect(jsonPath("$.data.post.text").value("Hello World!"));

    }
}
