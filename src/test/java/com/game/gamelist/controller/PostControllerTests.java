package com.game.gamelist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.gamelist.config.SecurityTestConfig;
import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.Role;
import com.game.gamelist.entity.User;
import com.game.gamelist.service.PostService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import org.mockito.Mockito;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    void shouldReturn_NewPost_When_sendPostRequest() throws Exception {
        User principal = User.builder().id(999L).username("testuser").password("testuser").email("testuser@gmail.com").userPicture("testuser").bio("testuser").bannerPicture("testuser").isActive(true).roles(Set.of(Role.ROLE_USER)).build();

        auth0JwtTestUtils.mockAuthentication(principal);

        Post mockPost = Post.builder().id(66L).text("Hello World!").user(principal).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        when(postService.createPost(Mockito.any(Post.class), Mockito.any(User.class))).thenReturn(mockPost);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/posts/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mockPost)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.post.id").value(66L))
                .andExpect(jsonPath("$.data.post.text").value("Hello World!"));

    }

    @Test
    void shouldReturn_allPost_when_sendGetRequest() throws Exception {
        User principal = User.builder().id(999L).username("testuser").password("testuser").email("testuser@gmail.com").userPicture("testuser").bio("testuser").bannerPicture("testuser").isActive(true).roles(Set.of(Role.ROLE_USER)).build();

        User notPrincipal = User.builder().id(998L).username("testuser2").password("testuser2").email("testuser2@gmail.com").userPicture("testuser2").bio("testuser2").bannerPicture("testuser2").isActive(true).roles(Set.of(Role.ROLE_USER)).build();

        Post notPrincipalPost = Post.builder().id(65L).text("Not a Post Belongs to Principal!").user(notPrincipal).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        auth0JwtTestUtils.mockAuthentication(principal);

        Post mockPost = Post.builder().id(66L).text("Hello World!").user(principal).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        Post mockPost2 = Post.builder().id(67L).text("MockPost 2!").user(principal).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        Post mockPost3 = Post.builder().id(68L).text("MockPost 3!").user(principal).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        Set<Post> mockPostsSet = Set.of(mockPost, mockPost2, mockPost3);
        List<Post> mockPostsList = new ArrayList<>(mockPostsSet);

        when(postService.findAllPostsByUserId(Mockito.any(User.class))).thenReturn(mockPostsSet);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.posts[0].id").value(mockPostsList.get(0).getId()))
                .andExpect(jsonPath("$.data.posts[0].text").value(mockPostsList.get(0).getText()))
                .andExpect(jsonPath("$.data.posts[1].id").value(mockPostsList.get(1).getId()))
                .andExpect(jsonPath("$.data.posts[1].text").value(mockPostsList.get(1).getText()))
                .andExpect(jsonPath("$.data.posts[2].id").value(mockPostsList.get(2).getId()))
                .andExpect(jsonPath("$.data.posts[2].text").value(mockPostsList.get(2).getText()))
                .andExpect(jsonPath("$.data.posts[3]").doesNotExist());
    }
}
