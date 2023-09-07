package com.game.gamelist.service.impl;

import com.game.gamelist.dto.GameDTO;
import com.game.gamelist.dto.UserGamesSummaryDTO;
import com.game.gamelist.entity.*;
import com.game.gamelist.exception.InvalidAuthorizationException;
import com.game.gamelist.exception.InvalidTokenException;
import com.game.gamelist.exception.ResourceNotFoundException;
import com.game.gamelist.mapper.GameMapper;
import com.game.gamelist.model.EditUserGameRequest;
import com.game.gamelist.repository.GameRepository;
import com.game.gamelist.repository.StatusUpdateRepository;
import com.game.gamelist.repository.UserGameRepository;
import com.game.gamelist.repository.UserRepository;
import com.game.gamelist.service.UserGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserGameServiceImpl implements UserGameService {
    private final UserRepository userRepository;
    private final UserGameRepository userGameRepository;
    private final GameRepository gameRepository;
    private final StatusUpdateRepository statusUpdateRepository;
    private final GameMapper gameMapper;

    @Override
    public UserGame findUserGameById(Long requestedId, User principal) {
        if (principal == null) {
            throw new InvalidTokenException("Invalid principal user");
        }

        Optional<UserGame> userGame = userGameRepository.findByGameIdAndUserId(requestedId, principal.getId());

        if (userGame.isPresent()) {
            return userGame.get();
        }

        throw new ResourceNotFoundException("UserGame not found with ID: " + requestedId + " and UserID: " + principal.getId());
    }



    @Override
    public UserGame createUserGame(UserGame userGame, User principal) {
        if (principal == null) {
            throw new InvalidTokenException("Invalid token");
        }

        // Check if the UserGame already exists in the database
        UserGame existingUserGame = userGameRepository.findFirstByUserIdAndGameId(principal.getId(), userGame.getGame().getId());
        StatusUpdate statusUpdate = new StatusUpdate();

        if (existingUserGame != null) {
            // If the UserGame already exists, update the existing instance

            if(existingUserGame.getGameStatus() != userGame.getGameStatus()) {
                statusUpdate.setUserGame(existingUserGame);
                statusUpdate.setGameStatus(userGame.getGameStatus());
                statusUpdateRepository.save(statusUpdate);
            }
            existingUserGame.setIsPrivate(userGame.getIsPrivate());
            existingUserGame.setRating(userGame.getRating());
            existingUserGame.setStartDate(userGame.getStartDate());
            existingUserGame.setCompletedDate(userGame.getCompletedDate());
            existingUserGame.setGameStatus(userGame.getGameStatus());
            existingUserGame.setGameNote(userGame.getGameNote());
            userGameRepository.save(existingUserGame);

            return existingUserGame;
        } else {
            // If the UserGame does not exist, create a new instance
            Game game = gameRepository.findById(userGame.getGame().getId()).orElseThrow(() -> new ResourceNotFoundException("Game not found with ID: " + userGame.getGame().getId()));

            userGame.setUser(principal);
            userGame.setGame(game);

            statusUpdate.setUserGame(userGame);
            statusUpdate.setGameStatus(userGame.getGameStatus());
            userGameRepository.save(userGame);
            statusUpdateRepository.save(statusUpdate);
            return userGame;
        }
    }

    @Override
    public UserGame updateUserGameById(EditUserGameRequest userGame, User principal) {
        if (principal == null) throw new InvalidTokenException("Invalid token");

        Optional<UserGame> userGameOptional = userGameRepository.findByGameIdAndUserId(userGame.getGameId(), principal.getId());

        if (userGameOptional.isPresent()) {
            UserGame responseData = userGameOptional.get();

            if(userGame.getGameStatus() != responseData.getGameStatus()) {
                StatusUpdate statusUpdate = new StatusUpdate();
                statusUpdate.setUserGame(responseData);
                statusUpdate.setGameStatus(userGame.getGameStatus());
                statusUpdateRepository.save(statusUpdate);
            }

            responseData.setGameStatus(userGame.getGameStatus());
            responseData.setGameNote(userGame.getGameNote());
            responseData.setIsPrivate(userGame.getIsPrivate());
            responseData.setRating(userGame.getRating());
            responseData.setCompletedDate(userGame.getCompletedDate());
            responseData.setStartDate(userGame.getStartDate());

            return userGameRepository.save(responseData);
        }

        throw new ResourceNotFoundException("UserGame not found with ID: " + userGame.getGameId());
    }

    @Override
    public UserGame deleteUserGameById(Long requestedId, User principal) {
        if (principal == null) throw new InvalidTokenException("Invalid token");

        Optional<UserGame> userGameOptional = userGameRepository.findById(requestedId);

        if (userGameOptional.isPresent()) {
            UserGame responseData = userGameOptional.get();
            User user = responseData.getUser();

            if (principal.getId().equals(user.getId())) {
                return resetUserGameAndStatusUpdate(responseData);
            }
            throw new InvalidAuthorizationException("Invalid authorization");
        }

        throw new ResourceNotFoundException("UserGame not found with ID: " + requestedId);
    }

    @Override
    public Set<UserGame> findAllUserGamesByUserId(User principal) {
        Optional<Set<UserGame>> optionalUserGames = userGameRepository.findAllByUserId(principal.getId());

        if (optionalUserGames.isPresent()) {
            return optionalUserGames.get();
        }

        throw new ResourceNotFoundException("UserGame not found with ID: " + principal.getId());
    }

    @Override
    public UserGamesSummaryDTO findAllUserGamesByUserIdByStatus(User principal) {
        List<Game> playingGames = gameRepository.findGamesByUserIdAndStatus(principal.getId(), GameStatus.Playing);
        List<GameDTO> playingGameDTOs = gameMapper.gamesToGameDTOs(playingGames);
        List<Game> completedGames = gameRepository.findGamesByUserIdAndStatus(principal.getId(), GameStatus.Completed);
        List<GameDTO> completedGameDTOs = gameMapper.gamesToGameDTOs(completedGames);
        List<Game> pausedGames = gameRepository.findGamesByUserIdAndStatus(principal.getId(), GameStatus.Paused);
        List<GameDTO> pausedGameDTOs = gameMapper.gamesToGameDTOs(pausedGames);
        List<Game> planningGames = gameRepository.findGamesByUserIdAndStatus(principal.getId(), GameStatus.Planning);
        List<GameDTO> planningGameDTOs = gameMapper.gamesToGameDTOs(planningGames);
        List<Game> dropGames = gameRepository.findGamesByUserIdAndStatus(principal.getId(), GameStatus.Dropped);
        List<GameDTO> dropGameDTOs = gameMapper.gamesToGameDTOs(dropGames);
        List<Game> justAdded = gameRepository.findGamesByUserIdAndStatus(principal.getId(), GameStatus.JustAdded);
        List<GameDTO> justAddedGameDTOs = gameMapper.gamesToGameDTOs(justAdded);

        UserGamesSummaryDTO userGamesSummary = new UserGamesSummaryDTO();
        userGamesSummary.setPlaying(playingGameDTOs);
        userGamesSummary.setPlayingCount(playingGameDTOs.size());
        userGamesSummary.setCompleted(completedGameDTOs);
        userGamesSummary.setCompletedCount(completedGameDTOs.size());
        userGamesSummary.setPaused(pausedGameDTOs);
        userGamesSummary.setPausedCount(pausedGameDTOs.size());
        userGamesSummary.setPlanning(planningGameDTOs);
        userGamesSummary.setPlanningCount(planningGameDTOs.size());
        userGamesSummary.setDropped(dropGameDTOs);
        userGamesSummary.setDroppedCount(dropGameDTOs.size());
        userGamesSummary.setJustAdded(justAddedGameDTOs);
        userGamesSummary.setJustAddedCount(justAddedGameDTOs.size());

        int totalCount = playingGameDTOs.size() + completedGameDTOs.size() + pausedGameDTOs.size() + planningGameDTOs.size() + dropGameDTOs.size() + justAddedGameDTOs.size();
        userGamesSummary.setTotalCount(totalCount);

        String listsOrder = userRepository.findListsOrderById(principal.getId());
        userGamesSummary.setListsOrder(listsOrder);

        return userGamesSummary;
    }

    @Override
    public UserGame findUserGameByGameId(Long gameId, User principal) {
        Optional<UserGame> userGameOptional = userGameRepository.findByGameIdAndUserId(gameId, principal.getId());

        if (userGameOptional.isPresent()) {
            UserGame responseData = userGameOptional.get();

            if (responseData.getGameStatus() == GameStatus.JustAdded) {
                responseData.setGameStatus(null);
            }
            return userGameOptional.get();
        }

        return UserGame.builder().gameStatus(GameStatus.Inactive).gameNote("").isPrivate(false).gameNote("").game(gameRepository.findById(gameId).orElseThrow(() -> new ResourceNotFoundException("Game can not find by ID: " + gameId))).rating(null).user(principal).build();
    }

    @Override
    public UserGame deleteUserGameByGameId(Long gameId, User principal) {
        if (principal == null) throw new InvalidTokenException("Invalid token");

        Optional<UserGame> userGameOptional = userGameRepository.findByGameIdAndUserId(gameId, principal.getId());

        if (userGameOptional.isPresent()) {
            UserGame responseData = userGameOptional.get();
            return resetUserGameAndStatusUpdate(responseData);
        }
        throw new ResourceNotFoundException("UserGame not found with Game ID: " + gameId + " and User ID: " + principal.getId());
    }

    private UserGame resetUserGameAndStatusUpdate(UserGame userGame) {
        userGame.setGameStatus(GameStatus.Inactive);
        userGame.setGameNote(null);
        userGame.setRating(null);
        userGame.setCompletedDate(null);
        userGame.setStartDate(null);
        userGame.setIsPrivate(false);

        StatusUpdate statusUpdate = new StatusUpdate();
        statusUpdate.setUserGame(userGame);
        statusUpdate.setGameStatus(userGame.getGameStatus());
        statusUpdateRepository.save(statusUpdate);
        return userGameRepository.save(userGame);
    }
}
