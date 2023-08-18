package com.game.gamelist.service.impl;

import com.game.gamelist.entity.Game;
import com.game.gamelist.entity.GameStatus;
import com.game.gamelist.entity.User;
import com.game.gamelist.entity.UserGame;
import com.game.gamelist.exception.InvalidAuthorizationException;
import com.game.gamelist.exception.InvalidTokenException;
import com.game.gamelist.exception.ResourceNotFoundException;
import com.game.gamelist.repository.GameRepository;
import com.game.gamelist.repository.UserGameRepository;
import com.game.gamelist.service.UserGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserGameServiceImpl implements UserGameService {
    private final UserGameRepository userGameRepository;
    private final GameRepository gameRepository;

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

        System.out.println("Principal UserName: 👹👹👹👹👹👹👹👹👹👹👹👹" + principal.getUsername());
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
}
