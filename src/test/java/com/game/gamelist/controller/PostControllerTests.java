package com.game.gamelist.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.gamelist.config.SecurityTestConfig;
import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.Role;
import com.game.gamelist.entity.User;
import com.game.gamelist.model.MockPostView;
import com.game.gamelist.model.MockUserBasicView;
import com.game.gamelist.projection.PostView;
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

    User principal;
    User notPrincipal;
    MockUserBasicView mockPrincipal;
    MockUserBasicView mockNonPrincipal;
    MockPostView notPrincipalPost;
    MockPostView mockPost;
    MockPostView mockPost2;
    MockPostView mockPost3;

    @BeforeEach
    void beforeEachTest() {
         principal = User.builder().id(999L).username("testuser").password("testuser").email("testuser@gmail.com").userPicture("testuser").bio("testuser").bannerPicture("testuser").isActive(true).roles(Set.of(Role.ROLE_USER)).build();

        mockPrincipal = MockUserBasicView.builder().id(999L).username("testuser").userPicture("testuser").build();

         notPrincipal = User.builder().id(998L).username("testuser2").password("testuser2").email("testuser2@gmail.com").userPicture("testuser2").bio("testuser2").bannerPicture("testuser2").isActive(true).roles(Set.of(Role.ROLE_USER)).build();

         mockNonPrincipal = MockUserBasicView.builder().id(998L).username("testuser2").userPicture("testuser2").build();

         notPrincipalPost = MockPostView.builder().id(65L).text("Not Principal Post").user(mockNonPrincipal).createdAt(LocalDateTime.now()).build();

        auth0JwtTestUtils.mockAuthentication(principal);

         mockPost = MockPostView.builder().id(66L).text("Hello World!").user(mockPrincipal).createdAt(LocalDateTime.now()).likes(new ArrayList<>()).build();

         mockPost2 = MockPostView.builder().id(67L).text("MockPost 2!").user(mockPrincipal).createdAt(LocalDateTime.now()).build();

         mockPost3 = MockPostView.builder().id(68L).text("MockPost 3!").user(mockPrincipal).createdAt(LocalDateTime.now()).build();
    }

    @Test
    void shouldReturn_NewPost_When_sendPostRequest() throws Exception {

        MockPostView createdPost = MockPostView.builder().id(69L).text("New Created Post").user(mockPrincipal).createdAt(LocalDateTime.now()).likes(new ArrayList<>()).build();

        when(postService.createPost(Mockito.any(Post.class), Mockito.any(User.class))).thenReturn(createdPost);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdPost)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("Post created successfully"))
                .andExpect(jsonPath("$.data.post.id").value(69L))
                .andExpect(jsonPath("$.data.post.text").value("New Created Post"));

    }

    @Test
    void shouldReturn_allPost_when_sendGetRequest() throws Exception {

        List<PostView> mockPostsList = List.of(mockPost, mockPost2, mockPost3);

        when(postService.findAllPostsByUserId(Mockito.any(User.class))).thenReturn(mockPostsList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts")
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

    @Test
    void shouldReturn_AllPost_when_sentGetAllRequest() throws Exception {
        List<PostView> allPostList = List.of(notPrincipalPost, mockPost, mockPost2, mockPost3);

        when(postService.findAllPosts(principal)).thenReturn(allPostList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts/all")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.posts[0].id").value(allPostList.get(0).getId()))
                .andExpect(jsonPath("$.data.posts[0].text").value(allPostList.get(0).getText()))
                .andExpect(jsonPath("$.data.posts[1].id").value(allPostList.get(1).getId()))
                .andExpect(jsonPath("$.data.posts[1].text").value(allPostList.get(1).getText()))
                .andExpect(jsonPath("$.data.posts[2].id").value(allPostList.get(2).getId()))
                .andExpect(jsonPath("$.data.posts[2].text").value(allPostList.get(2).getText()))
                .andExpect(jsonPath("$.data.posts[3].id").value(allPostList.get(3).getId()))
                .andExpect(jsonPath("$.data.posts[3].text").value(allPostList.get(3).getText()))
                .andExpect(jsonPath("$.data.posts[4]").doesNotExist());
    }

    @Test
    void shouldReturn_post_when_sendGetByIdRequest() throws Exception {

        when(postService.findPostById(Mockito.anyLong(), Mockito.any(User.class))).thenReturn(mockPost);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/posts/66")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.post.id").value(66L))
                .andExpect(jsonPath("$.data.post.text").value("Hello World!"));
    }

    @Test
    void shouldReturn_post_when_sendPutRequestById() throws Exception {

        PostView updatedPost = MockPostView.builder().id(66L).text("Post Got Updated").user(mockPrincipal).createdAt(LocalDateTime.now()).build();

            when(postService.updatePostById(Mockito.anyLong(), Mockito.any(Post.class), Mockito.any(User.class))).thenReturn(updatedPost);
            mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/posts/66")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(mockPost)))
                    .andExpect(status().isOk()).andExpect(jsonPath("$.message").value("Post updated successfully"))
                    .andExpect(jsonPath("$.data.post.id").value(66L))
                    .andExpect(jsonPath("$.data.post.text").value("Post Got Updated"));
    }

    @Test
    void shouldReturn_NO_CONTENT_when_sendDeleteRequestById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/posts/66")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Post deleted successfully"))
                .andExpect(jsonPath("$.status").value("NO_CONTENT"))
                .andExpect(jsonPath("$.data").doesNotExist()).andExpect(jsonPath("$.statusCode").value(204));
    }
}
