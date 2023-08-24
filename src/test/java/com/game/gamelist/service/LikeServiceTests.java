package com.game.gamelist.service;

import com.game.gamelist.entity.LikeEntity;
import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.User;
import com.game.gamelist.model.*;
import com.game.gamelist.repository.InteractiveEntityRepository;
import com.game.gamelist.repository.LikeRepository;
import com.game.gamelist.repository.UserRepository;
import com.game.gamelist.service.impl.LikeServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class LikeServiceTests {

    @InjectMocks
    private LikeServiceImpl likeService;
    @Mock
    private LikeRepository likeRepository;
    @Mock
    private InteractiveEntityRepository interactiveEntityRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    void when_createLike_should_return_additional_like() {
        // Arrange
        User principle = User.builder().id(10L).username("changli").email("changli@gmail.com").password("123456").build();
        MockUserBasicView mockPrincipleBasic = MockUserBasicView.builder().id(10L).username("changli").build();
        Post post = Post.builder().id(123L).text("Hello World!").user(principle).build();
        MockPostView mockPost = MockPostView.builder().id(123L).text("Hello World!").user(mockPrincipleBasic).build();
        MockLikeEntitiyView mockLike = MockLikeEntitiyView.builder().id(1L).user(mockPrincipleBasic).build();
        LikeEntity like = LikeEntity.builder().id(1L).user(principle).interactiveEntity(post).build();

        when(likeRepository.existsByUserIdAndInteractiveEntityId(principle.getId(), post.getId())).thenReturn(false);
        when(userRepository.findById(principle.getId())).thenReturn(Optional.of(principle));
        when(interactiveEntityRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(likeRepository.save(Mockito.any(LikeEntity.class))).thenReturn(like);
        when(likeRepository.findProjectedById(like.getId())).thenReturn(mockLike);

        // Act
        LikeEntityView createdLike = likeService.createLike(principle, post.getId());

        // Assert

        Assertions.assertEquals(createdLike.getId(), mockLike.getId());
        Assertions.assertEquals(createdLike.getUser().getId(), mockLike.getUser().getId());
    }

    @Test
    void when_deleteLikById_should_return_nothing_but_delete_like() {
        // Arrange
        User principle = User.builder().id(10L).username("changli").email("changli@gmail.com").password("123456").build();
        MockUserBasicView mockPrincipleBasic = MockUserBasicView.builder().id(10L).username("changli").build();
        Post post = Post.builder().id(123L).text("Hello World!").user(principle).build();
        MockPostView mockPost = MockPostView.builder().id(123L).text("Hello World!").user(mockPrincipleBasic).build();
        MockLikeEntitiyView mockLike = MockLikeEntitiyView.builder().id(1L).user(mockPrincipleBasic).build();
        LikeEntity like = LikeEntity.builder().id(1L).user(principle).interactiveEntity(post).build();

        when(likeRepository.existsByUserIdAndInteractiveEntityId(principle.getId(), post.getId())).thenReturn(true);
        // Act

        likeService.deleteLikeById(principle, post.getId());
        // Assert

        Mockito.verify(likeRepository, Mockito.times(1)).deleteByUserIdAndInteractiveEntityId(principle.getId(), post.getId());
    }

}
