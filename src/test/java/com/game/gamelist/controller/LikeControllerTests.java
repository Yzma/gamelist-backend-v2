package com.game.gamelist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.gamelist.config.SecurityTestConfig;
import com.game.gamelist.entity.LikeEntity;
import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.Role;
import com.game.gamelist.entity.User;
import com.game.gamelist.model.MockLikeEntitiyView;
import com.game.gamelist.model.MockPostView;
import com.game.gamelist.model.MockUserBasicView;

import com.game.gamelist.service.LikeService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.mockito.InjectMocks;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.util.Set;

import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@Import(SecurityTestConfig.class)
public class LikeControllerTests {

    @InjectMocks
    private LikeController likeController;
    @MockBean
    private LikeService likeService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SecurityTestConfig.Auth0JwtTestUtils auth0JwtTestUtils;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(likeController);
    }

    @Test
     void when_createLike_should_return_additional_like() throws Exception {
        // Arrange
        User principal = User.builder().id(10L).username("testuser").password("testuser").email("testuser@gmail.com").userPicture("testuser").bio("testuser").bannerPicture("testuser").isActive(true).roles(Set.of(Role.ROLE_USER)).build();
        MockUserBasicView mockPrincipleBasic = MockUserBasicView.builder().id(10L).username("changli").userPicture("mock picture").bannerPicture("mock banner").build();
        Post post = Post.builder().id(123L).text("Hello World!").user(principal).build();
        MockPostView mockPost = MockPostView.builder().id(123L).text("Hello World!").user(mockPrincipleBasic).build();
        MockLikeEntitiyView mockLike = MockLikeEntitiyView.builder().id(1L).user(mockPrincipleBasic).build();
        LikeEntity like = LikeEntity.builder().id(1L).user(principal).interactiveEntity(post).build();

        auth0JwtTestUtils.mockAuthentication(principal);

        when(likeService.createLike(Mockito.any(User.class), Mockito.anyLong())).thenReturn(mockLike);
        // Act
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/likes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(123L)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.like.id").value(1L))
                .andExpect(jsonPath("$.data.like.user.id").value(10L))
                .andExpect(jsonPath("$.data.like.user.username").value("changli")).andExpect(jsonPath("$.data.like.user.userPicture").value("mock picture")).andExpect(jsonPath("$.data.like.user.bannerPicture").value("mock banner"));

    }

    @Test
    void when_deleteLike_should_return_no_content() throws Exception {
        // Arrange
        User principal = User.builder().id(10L).username("testuser").password("testuser").email("testuser@gmail.com").userPicture("testuser").bio("testuser").bannerPicture("testuser").isActive(true).roles(Set.of(Role.ROLE_USER)).build();
        MockUserBasicView mockPrincipleBasic = MockUserBasicView.builder().id(10L).username("changli").userPicture("mock picture").bannerPicture("mock banner").build();
        Post post = Post.builder().id(123L).text("Hello World!").user(principal).build();
        MockPostView mockPost = MockPostView.builder().id(123L).text("Hello World!").user(mockPrincipleBasic).build();
        MockLikeEntitiyView mockLike = MockLikeEntitiyView.builder().id(1L).user(mockPrincipleBasic).build();
        LikeEntity like = LikeEntity.builder().id(1L).user(principal).interactiveEntity(post).build();

        auth0JwtTestUtils.mockAuthentication(principal);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/likes/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.statusCode").value(204)).andExpect(jsonPath("$.message").value("Like deleted successfully"));
    }
}
