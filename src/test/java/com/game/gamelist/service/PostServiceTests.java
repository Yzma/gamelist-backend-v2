package com.game.gamelist.service;

import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.User;
import com.game.gamelist.repository.PostRepository;
import com.game.gamelist.repository.UserRepository;
import com.game.gamelist.service.impl.PostServiceImpl;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class PostServiceTests {

    @InjectMocks
    private PostServiceImpl postService;

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @BeforeEach


    @Test
    void when_createPost_shouldReturn_onePost() {
        // Arrange
        final var userToSave = User.builder().email("changli@gmail.com").username("changli").password("123456").createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        final var postToSave = Post.builder().text("Hello World!").user(userToSave).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
        when(postRepository.save(Mockito.any(Post.class))).thenReturn(postToSave);

        // Act
        Post createdPost = postService.createPost(postToSave, userToSave);

        // Assert
        Assertions.assertThat(createdPost).isNotNull();
        Assertions.assertThat(createdPost.getText()).isEqualTo("Hello World!");
        Assertions.assertThat(createdPost.getUser()).isEqualTo(userToSave);
    }

    @Test
    void when_findPostById_shouldReturn_onePost() {
        // Arrange
        final var userToSave = User.builder().id(123L).email("changli@gmail.com").username("changli").password("123456").createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        final var postToSave = Post.builder().id(999L).text("Hello World!").user(userToSave).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
        when(postRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(postToSave));

        // Act
        Post foundPost = postService.findPostById(231L, userToSave);

        // Assert
        Assertions.assertThat(foundPost).isNotNull();
        Assertions.assertThat(foundPost.getId()).isEqualTo(999L);
        Assertions.assertThat(foundPost.getText()).isEqualTo("Hello World!");
        Assertions.assertThat(foundPost.getUser()).isEqualTo(userToSave);
    }

    @Test
    void when_updatePosyById_shouldReturn_updatedPost() {
        // Arrange
        final var userToSave = User.builder().id(123L).email("changli@gmail.com").username("changli").password("123456").createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        final var updatedUser = User.builder().id(123L).email("updateUser@gamil.com").username("updatedUser").password("123456").createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        final var postToSave = Post.builder().id(999L).text("Hello World!").user(userToSave).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        final var postToUpdate = Post.builder().id(999L).text("An Updated Post").user(updatedUser).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
        when(postRepository.findById(Mockito.anyLong())).thenReturn(java.util.Optional.of(postToSave));
        when(postRepository.save(Mockito.any(Post.class))).thenReturn(postToUpdate);

        // Act
        Post updatedPost = postService.updatePostById(999L, postToUpdate, userToSave);

        // Assert
        Assertions.assertThat(updatedPost).isNotNull();
        Assertions.assertThat(updatedPost.getId()).isEqualTo(999L);
        Assertions.assertThat(updatedPost.getText()).isEqualTo("An Updated Post");
        Assertions.assertThat(updatedPost.getUser()).isEqualTo(updatedUser);
        Assertions.assertThat(updatedPost.getUser()).isNotEqualTo(userToSave);
    }

}

