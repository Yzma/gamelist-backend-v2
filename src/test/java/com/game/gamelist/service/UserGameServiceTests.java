package com.game.gamelist.service;

import com.game.gamelist.entity.*;
import com.game.gamelist.exception.InvalidAuthorizationException;
import com.game.gamelist.exception.InvalidTokenException;
import com.game.gamelist.exception.ResourceNotFoundException;
import com.game.gamelist.repository.GameRepository;
import com.game.gamelist.repository.StatusUpdateRepository;
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
import java.util.Set;

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
    @Mock
    private StatusUpdateRepository statusUpdateRepository;

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

        StatusUpdate statusUpdate = StatusUpdate.builder().gameStatus(GameStatus.Completed).userGame(userGameExisting).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        when(userGameRepository.findFirstByUserIdAndGameId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(userGameExisting);
        when(userGameRepository.save(Mockito.any(UserGame.class))).thenReturn(userGamePassed);
        when(statusUpdateRepository.save(Mockito.any(StatusUpdate.class))).thenReturn(statusUpdate);

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

        final var statusUpdate = StatusUpdate.builder().gameStatus(GameStatus.Completed).userGame(userGamePassed).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        when(userGameRepository.findFirstByUserIdAndGameId(Mockito.anyLong(), Mockito.anyLong())).thenReturn(null);
        when(gameRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(gameToSave));
        when(userGameRepository.save(Mockito.any(UserGame.class))).thenReturn(userGamePassed);
        when(statusUpdateRepository.save(Mockito.any(StatusUpdate.class))).thenReturn(statusUpdate);

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

    @Test
    void when_findUserGameById_by_non_existing_userGame_throw_ResourceNotFoundException() {

        when(userGameRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> userGameService.findUserGameById(999L, userToSave))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("UserGame not found");
    }

    @Test
    void when_updateUserGameById_should_return_updatedUserGame() {
        final var userGameExisting = UserGame.builder().id(999L).game(gameToSave).user(userToSave).gameNote("GameNote from userGameExisting").gameStatus(GameStatus.Paused).updatedAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        final var userGamePassed = UserGame.builder().id(999L).game(gameToSave).user(userToSave).gameNote("GameNote from userGameUpdated").gameStatus(GameStatus.Completed).updatedAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        when(userGameRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userGameExisting));
        when(userGameRepository.save(Mockito.any(UserGame.class))).thenReturn(userGamePassed);

        UserGame updatedUserGame = userGameService.updateUserGameById(999L, userGamePassed, userToSave);

        Assertions.assertThat(updatedUserGame.getId()).isNotNull();
        Assertions.assertThat(updatedUserGame.getId()).isEqualTo(999L);
        Assertions.assertThat(updatedUserGame.getGame()).isEqualTo(gameToSave);
        Assertions.assertThat(updatedUserGame.getUser()).isEqualTo(userToSave);
        Assertions.assertThat(updatedUserGame.getGameStatus()).isEqualTo(GameStatus.Completed);
        Assertions.assertThat(updatedUserGame.getGameNote()).isEqualTo("GameNote from userGameUpdated");
    }

    @Test
    void when_deleteUserGameById_should_return_deletedUserGame() {
        final var userGameExisting = UserGame.builder().id(999L).game(gameToSave).user(userToSave).gameNote("GameNote from userGameExisting").gameStatus(GameStatus.Paused).updatedAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        final var userGameInactive = UserGame.builder().id(999L).game(gameToSave).user(userToSave).gameNote(null).gameStatus(GameStatus.Inactive).rating(0).completedDate(null).createdAt(null).updatedAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        when(userGameRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(userGameExisting));
        when(userGameRepository.save(Mockito.any(UserGame.class))).thenReturn(userGameInactive);

        UserGame deletedUserGame = userGameService.deleteUserGameById(999L, userToSave);

        Assertions.assertThat(deletedUserGame.getId()).isNotNull();
        Assertions.assertThat(deletedUserGame.getId()).isEqualTo(999L);
        Assertions.assertThat(deletedUserGame.getGame()).isEqualTo(gameToSave);
        Assertions.assertThat(deletedUserGame.getUser()).isEqualTo(userToSave);
        Assertions.assertThat(deletedUserGame.getGameStatus()).isEqualTo(GameStatus.Inactive);
        Assertions.assertThat(deletedUserGame.getGameNote()).isNull();
    }

    @Test
    void when_findAllUserGamesByUserId_should_return_Set_of_userGames() {
        final var gameExisting1 = Game.builder().id(11L).name("Game1").build();
        final var gameExisting2 = Game.builder().id(12L).name("Game2").build();
        final var gameExisting3 = Game.builder().id(13L).name("Game3").build();

        final var userGameExisting1 = UserGame.builder().id(996L).game(gameExisting1).user(userToSave).gameNote("GameNote from userGameExisting1").gameStatus(GameStatus.Completed).updatedAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
        final var userGameExisting2 = UserGame.builder().id(998L).game(gameExisting2).user(userToSave).gameNote("GameNote from userGameExisting2").gameStatus(GameStatus.Paused).updatedAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
        final var userGameExisting3 = UserGame.builder().id(997L).game(gameExisting3).user(userToSave).gameNote("GameNote from userGameExisting3").gameStatus(GameStatus.Planning).updatedAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();

        Set<UserGame> userGameSet = Set.of(userGameExisting1, userGameExisting2, userGameExisting3);

        when(userGameRepository.findAllByUserId(Mockito.anyLong())).thenReturn(Optional.of(userGameSet));

        Set<UserGame> userGameSetFromService = userGameService.findAllUserGamesByUserId(userToSave);

        Assertions.assertThat(userGameSetFromService).isNotNull();
        Assertions.assertThat(userGameSetFromService).isNotEmpty();
        Assertions.assertThat(userGameSetFromService).hasSize(3);
        Assertions.assertThat(userGameSetFromService).contains(userGameExisting1, userGameExisting2, userGameExisting3);
        Assertions.assertThat(userGameSetFromService).containsExactlyInAnyOrderElementsOf(userGameSet);
    }
}
