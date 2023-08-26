package com.game.gamelist.service.impl;

import com.game.gamelist.dto.GameDTO;
import com.game.gamelist.dto.UserGamesSummaryDTO;
import com.game.gamelist.entity.Game;
import com.game.gamelist.entity.GameStatus;
import com.game.gamelist.entity.User;
import com.game.gamelist.entity.UserGame;
import com.game.gamelist.exception.InvalidAuthorizationException;
import com.game.gamelist.exception.InvalidTokenException;
import com.game.gamelist.exception.ResourceNotFoundException;
import com.game.gamelist.mapper.GameMapper;
import com.game.gamelist.repository.GameRepository;
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
    private final GameMapper gameMapper;

    @Override
    public UserGame findUserGameById(Long requestedId, User principal) {
        if (principal == null) {
            throw new InvalidTokenException("Invalid token");
        }

        Optional<UserGame> userGame = userGameRepository.findById(requestedId);

        if (userGame.isPresent()) {
            UserGame responseData = userGame.get();

            User user = responseData.getUser();

            if (principal.getId().equals(user.getId())) {
                return userGame.get();
            }
            throw new InvalidAuthorizationException("Invalid authorization");
        }
        throw new ResourceNotFoundException("UserGame not found with ID: " + requestedId);
    }

    @Override
    public UserGame createUserGame(UserGame userGame, User principal) {
        if (principal == null) {
            throw new InvalidTokenException("Invalid token");
        }

        // Check if the UserGame already exists in the database
        UserGame existingUserGame = userGameRepository.findFirstByUserIdAndGameId(principal.getId(), userGame.getGame().getId());

        if (existingUserGame != null) {
            // If the UserGame already exists, update the existing instance
            existingUserGame.setIsPrivate(userGame.getIsPrivate());
            existingUserGame.setRating(userGame.getRating());
            existingUserGame.setStartDate(userGame.getStartDate());
            existingUserGame.setCompletedDate(userGame.getCompletedDate());
            existingUserGame.setGameStatus(userGame.getGameStatus());
            existingUserGame.setGameNote(userGame.getGameNote());
            return userGameRepository.save(existingUserGame);
        } else {
            // If the UserGame does not exist, create a new instance
            Game game = gameRepository.findById(userGame.getGame().getId()).orElseThrow(() -> new ResourceNotFoundException("Game not found with ID: " + userGame.getGame().getId()));

            userGame.setUser(principal);
            userGame.setGame(game);
            return userGameRepository.save(userGame);
        }
    }

    @Override
    public UserGame updateUserGameById(Long requestedId, UserGame userGame, User principal) {
        if (principal == null) throw new InvalidTokenException("Invalid token");
//        Get the UserGame instance needed to be updated
        Optional<UserGame> userGameOptional = userGameRepository.findById(requestedId);

        if (userGameOptional.isPresent()) {
            UserGame responseData = userGameOptional.get();
            Game game = responseData.getGame();
            User user = responseData.getUser();
//            check if user id and game id matches
            if (!principal.getId().equals(user.getId()) || !game.getId().equals(userGame.getGame().getId())) {
                throw new InvalidAuthorizationException("Invalid authorization");
            }

            responseData.setGameStatus(userGame.getGameStatus());
            responseData.setGameNote(userGame.getGameNote());
            responseData.setIsPrivate(userGame.getIsPrivate());
            responseData.setRating(userGame.getRating());
            responseData.setCompletedDate(userGame.getCompletedDate());
            responseData.setUpdatedAt(userGame.getUpdatedAt());

            return userGameRepository.save(responseData);
        }

        throw new ResourceNotFoundException("UserGame not found with ID: " + requestedId);
    }

    @Override
    public UserGame deleteUserGameById(Long requestedId, User principal) {
        if (principal == null) throw new InvalidTokenException("Invalid token");

        Optional<UserGame> userGameOptional = userGameRepository.findById(requestedId);

        if (userGameOptional.isPresent()) {
            UserGame responseData = userGameOptional.get();
            User user = responseData.getUser();

            if (principal.getId().equals(user.getId())) {
                responseData.setGameStatus(GameStatus.Inactive);
                responseData.setGameNote(null);
                responseData.setRating(0);
                responseData.setCompletedDate(null);
                responseData.setStartDate(null);

                return userGameRepository.save(responseData);
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
        System.out.println("👹👹👹👹👹👹👹👹👹👹👹👹👹");

        List<Game> completedGames = gameRepository.findGamesByUserIdAndStatus(principal.getId(), GameStatus.Completed);
        List<GameDTO> completedGameDTOs = gameMapper.gamesToGameDTOs(completedGames);
        List<Game> pausedGames = gameRepository.findGamesByUserIdAndStatus(principal.getId(), GameStatus.Paused);
        List<GameDTO> pausedGameDTOs = gameMapper.gamesToGameDTOs(pausedGames);
        List<Game> planningGames = gameRepository.findGamesByUserIdAndStatus(principal.getId(), GameStatus.Planning);
        List<GameDTO> planningGameDTOs = gameMapper.gamesToGameDTOs(planningGames);
        List<Game> dropGames = gameRepository.findGamesByUserIdAndStatus(principal.getId(), GameStatus.Dropped);
        List<GameDTO> dropGameDTOs = gameMapper.gamesToGameDTOs(dropGames);
        List<Game> inactiveGames = gameRepository.findGamesByUserIdAndStatus(principal.getId(), GameStatus.Inactive);
        List<GameDTO> inactiveGameDTOs = gameMapper.gamesToGameDTOs(inactiveGames);

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
        userGamesSummary.setInactive(inactiveGameDTOs);
        userGamesSummary.setInactiveCount(inactiveGameDTOs.size());

        int totalCount = playingGameDTOs.size() + completedGameDTOs.size() + pausedGameDTOs.size() + planningGameDTOs.size() + dropGameDTOs.size() + inactiveGameDTOs.size();
        userGamesSummary.setTotalCount(totalCount);

        String listsOrder = userRepository.findListsOrderById(principal.getId());
        userGamesSummary.setListsOrder(listsOrder);

        return userGamesSummary;
    }
}
