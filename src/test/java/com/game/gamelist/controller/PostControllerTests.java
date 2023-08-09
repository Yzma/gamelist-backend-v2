//package com.game.gamelist.controller;
//
//import com.game.gamelist.controller.PostController;
//import com.game.gamelist.entity.Post;
//import com.game.gamelist.service.PostService;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//import org.testcontainers.shaded.com.github.dockerjava.core.MediaType;
//
//@SpringBootTest
//@WebMvcTest(PostController.class)
//public class PostControllerTests {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private PostService postService;
//
//    @Test
//    void shouldReturnNewPostWhenPostIsCreated() throws Exception {
//        when(postService.createPost(any(Post.class))).thenReturn(new Post(1L, "Hello World!"));
//
//        this.mockMvc.perform(post("/api/v1/posts")
//                        .contentType(APPLICATION_JSON).content("""
//                                {
//                                    "text": "Hello World!"
//                                }
//                                """))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id", is(1)))
//                .andExpect(jsonPath("$.text", is("Hello World!")));
//
//    }
//
//}
