package com.game.gamelist.service;

import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.User;
import com.game.gamelist.model.MockPostView;
import com.game.gamelist.model.MockUserBasicView;
import com.game.gamelist.projection.PostView;
import com.game.gamelist.projection.UserBasicView;
import com.game.gamelist.repository.PostRepository;
import com.game.gamelist.service.impl.PostServiceImpl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class PostServiceTests {

    @InjectMocks
    private PostServiceImpl postService;

    @Mock
    private PostRepository postRepository;

    @Test
    void when_createPost_should_return_one_post() {
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
    void when_findPostById_should_return_one_post() {
        // Arrange
        final var userToSave = MockUserBasicView.builder().id(123L).username("changli").build();

        final var principleUser = User.builder().id(123L).email("principle@gmail.com").username("changli").build();

        final var postToSave = MockPostView.builder().id(999L).text("Hello World!").user((UserBasicView) userToSave).createdAt(LocalDateTime.now()).build();
        when(postRepository.findProjectedById(Mockito.anyLong())).thenReturn(java.util.Optional.of(postToSave));

        // Act
        PostView foundPost = postService.findPostById(999L, principleUser);

        // Assert
        Assertions.assertThat(foundPost).isNotNull();
        Assertions.assertThat(foundPost.getId()).isEqualTo(999L);
        Assertions.assertThat(foundPost.getText()).isEqualTo("Hello World!");
        Assertions.assertThat(foundPost.getUser()).isEqualTo(userToSave);
    }

    @Test
    void when_updatePosyById_should_return_updated_post() {
        // Arrange
        final var userToSave = User.builder().id(123L).email("changli@gmail.com").username("changli").password("123456").createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        final var userBasic = MockUserBasicView.builder().id(123L).username("changli").build();

        final var updatedUser = User.builder().id(124L).email("updateUser@gamil.com").username("updatedUser").password("123456").createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        final var postToSave = MockPostView.builder().id(999L).text("Hello World!").user(userBasic).createdAt(LocalDateTime.now()).build();

        final var postToUpdate = Post.builder().id(999L).text("An Updated Post").user(updatedUser).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
        when(postRepository.findProjectedById(Mockito.anyLong())).thenReturn(Optional.of(postToSave));
        when(postRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(postToUpdate));

        when(postRepository.save(Mockito.any(Post.class))).thenReturn(postToUpdate);

        // Act
        PostView updatedPost = postService.updatePostById(999L, postToUpdate, userToSave);

        // Assert
        Assertions.assertThat(updatedPost).isNotNull();
        Assertions.assertThat(updatedPost.getId()).isEqualTo(999L);
        Assertions.assertThat(updatedPost.getText()).isEqualTo("An Updated Post");
    }
    @Test
    void when_deletePostById_should_return_deleted_post() {
        // Arrange
        final var userToSave = User.builder().id(123L).email("changli@gmail.com").username("changli").password("123456").createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        final var postToSave = Post.builder().id(999L).text("Hello World!").user(userToSave).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();


        when(postRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(postToSave));

        // Act
         postService.deletePostById(999L, userToSave);

        Mockito.verify(postRepository).deleteById(999L);

    }

    @Test
    void when_findAllPosts_should_return_all_posts() {
        // Arrange
        final var userToSave = MockUserBasicView.builder().id(123L).username("changli").build();

        final var ownerToSave = User.builder().id(123L).email("changli").password("123456").build();

        final var postToSave1 = MockPostView.builder().id(999L).text("Hello World!").user(userToSave).createdAt(LocalDateTime.now()).build();

        final var postToSave2 = MockPostView.builder().id(222L).text("Java No.1!").user(userToSave).createdAt(LocalDateTime.now()).build();

        final var postToSave3 = MockPostView.builder().id(111L).text("Java No.1?").user(userToSave).createdAt(LocalDateTime.now()).build();

        when(postRepository.findAllPosts()).thenReturn(List.of(postToSave1, postToSave2, postToSave3));
        // Act
        List<PostView> foundPosts = postService.findAllPosts(ownerToSave);
        // Assert

        Assertions.assertThat(foundPosts).isNotNull();
        Assertions.assertThat(foundPosts.size()).isEqualTo(3);
        Assertions.assertThat(foundPosts.get(0).getId()).isEqualTo(999L);
        Assertions.assertThat(foundPosts.get(0).getText()).isEqualTo("Hello World!");
        Assertions.assertThat(foundPosts.get(0).getUser().getId()).isEqualTo(userToSave.getId());

        Assertions.assertThat(foundPosts.get(1).getId()).isEqualTo(222L);
        Assertions.assertThat(foundPosts.get(1).getText()).isEqualTo("Java No.1!");
        Assertions.assertThat(foundPosts.get(2).getUser().getId()).isEqualTo(userToSave.getId());
        Assertions.assertThat(foundPosts.get(2).getId()).isEqualTo(111L);
    }

    @Test
    void when_findAllPostsByUserId_return_allPostsBelongToUser() {
        // Arrange
        final var userToSave = User.builder().id(123L).email("changli@gmail.com").username("changli").password("123456").createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        final var userBasic = MockUserBasicView.builder().id(123L).username("changli").build();

        final var anotherUser = User.builder().id(124L).email("anotherUser@gamil.com").username("anotherUser").password("123456").createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        final var anotherUserBasic = MockUserBasicView.builder().id(124L).username("anotherUser").build();

        final var postToSave1 = MockPostView.builder().id(999L).text("Hello World!").user(userBasic).createdAt(LocalDateTime.now()).build();

        final var postToSave2 = MockPostView.builder().id(222L).text("Java No.1!").user(userBasic).createdAt(LocalDateTime.now()).build();

        final var postToSave3 = MockPostView.builder().id(111L).text("Java No.1?").user(userBasic).createdAt(LocalDateTime.now()).build();

        final var postToSave4 = MockPostView.builder().id(444L).text("Java No.1?").user(anotherUserBasic).createdAt(LocalDateTime.now()).build();

        when(postRepository.findAllProjectedByUserId(Mockito.anyLong())).thenReturn(Optional.of(List.of(postToSave1, postToSave2, postToSave3)));

        // Act
        List<PostView> foundPosts = postService.findAllPostsByUserId(userToSave);

        // Assert

        Assertions.assertThat(foundPosts).isNotNull();
        Assertions.assertThat(foundPosts.size()).isEqualTo(3);
        Assertions.assertThat(foundPosts).contains(postToSave1);
        Assertions.assertThat(foundPosts).contains(postToSave2);
        Assertions.assertThat(foundPosts).contains(postToSave3);
        Assertions.assertThat(foundPosts).doesNotContain(postToSave4);

        Assertions.assertThat(foundPosts.stream().filter(post -> post.getUser().equals(userBasic)).count()).isEqualTo(3);

    }
}

