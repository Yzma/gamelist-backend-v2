package com.game.gamelist.service;

import com.game.gamelist.entity.Game;
import com.game.gamelist.entity.GameStatus;
import com.game.gamelist.entity.User;
import com.game.gamelist.entity.UserGame;
import com.game.gamelist.exception.InvalidAuthorizationException;
import com.game.gamelist.exception.InvalidTokenException;
import com.game.gamelist.exception.ResourceNotFoundException;
import com.game.gamelist.repository.GameRepository;
import com.game.gamelist.repository.UserGameRepository;
import com.game.gamelist.service.impl.UserGameServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
public class UserGameServiceTests {

    @InjectMocks
    private UserGameServiceImpl userGameService;

    @Mock
    private UserGameRepository userGameRepository;
    @Mock
    private GameRepository gameRepository;

    private User userToSave;
    private Game gameToSave;

    @BeforeEach
    void setUp() {
        userToSave = User.builder().id(1L).email("changli@gmail.com").username("changli").password("123456").createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
        gameToSave = Game.builder().id(10L).name("Game 1").description("Game 1 Description").createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
    }

    @Test
    void when_createUserGame_with_anExistingUserGame_should_return_one_userGame() {
        // Arrange
        final var userGamePassed = UserGame.builder().gameNote("GameNote from userGameUpdated").gameStatus(GameStatus.Completed).user(userToSave).game(gameToSave).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        final var userGameExisting = UserGame.builder().id(999L).game(gameToSave).user(userToSave).gameNote("GameNote from userGameExisting").updatedAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        when(userGameRepository.findFirstByUserIdAndGameId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(userGameExisting);
        when(userGameRepository.save(Mockito.any(UserGame.class))).thenReturn(userGamePassed);

        // Act
        UserGame createdUserGame = userGameService.createUserGame(userGamePassed, userToSave);

        // Assert
        Assertions.assertThat(createdUserGame).isNotNull();
        Assertions.assertThat(createdUserGame.getUser()).isEqualTo(userToSave);
        Assertions.assertThat(createdUserGame.getGame()).isEqualTo(gameToSave);
        Assertions.assertThat(createdUserGame.getGameStatus()).isEqualTo(GameStatus.Completed);
    }

    @Test
    void when_createUserGame_with_aNewUserGame_should_return_one_userGame() {
        // Arrange
        final var userGamePassed = UserGame.builder().gameNote("GameNote from userGameUpdated").gameStatus(GameStatus.Completed).user(userToSave).game(gameToSave).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        when(userGameRepository.findFirstByUserIdAndGameId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(null);

        when(gameRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(gameToSave));
        when(userGameRepository.save(Mockito.any(UserGame.class))).thenReturn(userGamePassed);

        // Act
        UserGame createdUserGame = userGameService.createUserGame(userGamePassed, userToSave);

        // Assert
        Assertions.assertThat(createdUserGame).isNotNull();
        Assertions.assertThat(createdUserGame.getUser()).isEqualTo(userToSave);
        Assertions.assertThat(createdUserGame.getGame()).isEqualTo(gameToSave);
        Assertions.assertThat(createdUserGame.getGameStatus()).isEqualTo(GameStatus.Completed);
    }

    @Test
    void when_createUserGame_with_nullPrincipal_should_throw_InvalidTokenException () {
        final var userGamePassed = UserGame.builder().gameNote("GameNote from userGameUpdated").gameStatus(GameStatus.Completed).user(userToSave).game(gameToSave).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        // Act & Assert
        Assertions.assertThatThrownBy(() -> userGameService.createUserGame(userGamePassed, null))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining("Invalid token");
    }

    @Test
    void when_createUserGame_with_nullGame_should_throw_ResourceNotFoundException() {
        final var gameNotExisting = Game.builder().id(999L).name("Game 1").description("Game 1 Description").createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        final var userGamePassed = UserGame.builder().gameNote("GameNote from userGameUpdated").gameStatus(GameStatus.Completed).user(userToSave).game(gameToSave).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        when(userGameRepository.findFirstByUserIdAndGameId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(null);

        when(gameRepository.findById(Mockito.anyLong())).thenThrow(new ResourceNotFoundException("Game not found"));

        // Act & Assert

        Assertions.assertThatThrownBy(() -> userGameService.createUserGame(userGamePassed, userToSave))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Game not found");
    }

    @Test
    void when_findUseGameById_return_userGame() {
        final var userGameExisting = UserGame.builder().id(999L).game(gameToSave).user(userToSave).gameNote("GameNote from userGameExisting").gameStatus(GameStatus.Paused).updatedAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        when(userGameRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userGameExisting));

        UserGame userGameFound = userGameService.findUserGameById(999L, userToSave);

        Assertions.assertThat(userGameFound).isNotNull();
        Assertions.assertThat(userGameFound.getId()).isEqualTo(999L);
        Assertions.assertThat(userGameFound.getGame()).isEqualTo(gameToSave);
        Assertions.assertThat(userGameFound.getUser()).isEqualTo(userToSave);
        Assertions.assertThat(userGameFound.getGameStatus()).isEqualTo(GameStatus.Paused);
    }

    @Test
    void when_findUserGameById_by_non_owner_throw_InvalidAuthorizationException() {
        final var nonOwner = User.builder().id(2L).email("notowner@gmail.com").password("123456").username("notowner").createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        final var userGameExisting = UserGame.builder().id(999L).game(gameToSave).user(userToSave).gameNote("GameNote from userGameExisting").gameStatus(GameStatus.Paused).updatedAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        when(userGameRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userGameExisting));


        Assertions.assertThatThrownBy(() -> userGameService.findUserGameById(999L, nonOwner))
                .isInstanceOf(InvalidAuthorizationException.class)
                .hasMessageContaining("Invalid authorization");
    }

    
}
