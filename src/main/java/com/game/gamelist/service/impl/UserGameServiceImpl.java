package com.game.gamelist.service.impl;

import com.game.gamelist.entity.Game;
import com.game.gamelist.entity.GameStatus;
import com.game.gamelist.entity.User;
import com.game.gamelist.entity.UserGame;
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
    public Optional<UserGame> findUserGameById(Long requestedId, User principal) {

        if (principal == null) {
            return Optional.empty();
        }

        Optional<UserGame> userGame = userGameRepository.findById(requestedId);

        if (userGame.isPresent()) {
            UserGame responseData = userGame.get();
            User user = responseData.getUser();

            if (principal.getId().equals(user.getId())) {
                return userGame;
            }
        }

        return Optional.empty();
    }


    @Override
    public UserGame createUserGame(UserGame userGame, User principal) {
        if (principal == null) {
            return null;
        }

        // Check if the UserGame already exists in the database
        UserGame existingUserGame = userGameRepository.findFirstByUserIdAndGameId(principal.getId(), userGame.getGame().getId());

        if (existingUserGame != null) {

            existingUserGame.setGameStatus(userGame.getGameStatus());
            existingUserGame.setGameNote(userGame.getGameNote());
            return userGameRepository.save(existingUserGame);
        } else {
            // If the UserGame does not exist, create a new instance
            Game game = gameRepository.findById(userGame.getGame().getId()).orElse(null);
            if (game != null) {
                userGame.setUser(principal);
                userGame.setGame(game);
                return userGameRepository.save(userGame);
            } else {
                return null;
            }
        }
    }

    @Override
    public Optional<UserGame> updateUserGameById(Long requestedId, UserGame userGame, User principal) {
//        Get the UserGame instance needed to be updated
        Optional<UserGame> userGameOptional = userGameRepository.findById(requestedId);

        if (userGameOptional.isPresent()) {
            UserGame responseData = userGameOptional.get();
            Game game = responseData.getGame();
            User user = responseData.getUser();
//            check if user id and game id matches
            if (principal == null || !principal.getId().equals(user.getId()) || !game.getId().equals(userGame.getGame().getId())) {
                return Optional.empty();
            }

            responseData.setGameStatus(userGame.getGameStatus());
            responseData.setGameNote(userGame.getGameNote());
            responseData.setIsPrivate(userGame.getIsPrivate());
            responseData.setRating(userGame.getRating());
            responseData.setCompletedDate(userGame.getCompletedDate());
            responseData.setUpdatedAt(userGame.getUpdatedAt());

            return Optional.of(userGameRepository.save(responseData));
        }

        return Optional.empty();
    }

    @Override
    public Optional<UserGame> deleteUserGameById(Long requestedId, User principal) {
        if(principal == null) return Optional.empty();

        Optional<UserGame> userGameOptional = userGameRepository.findById(requestedId);

        if(userGameOptional.isPresent()) {
            UserGame responseData = userGameOptional.get();
            User user = responseData.getUser();

            if(principal.getId().equals(user.getId())) {
                responseData.setGameStatus(GameStatus.Inactive);
                responseData.setGameNote(null);
                responseData.setRating(0);
                responseData.setCompletedDate(null);
                responseData.setStartDate(null);

                return Optional.of(userGameRepository.save(responseData));
            }
        }

        return Optional.empty();
    }

    @Override
    public Optional<Set<UserGame>> findAllUserGamesByUserId(User principal) {

        if(principal == null) return Optional.empty();

        Optional<Set<UserGame>> optionalUserGames = userGameRepository.findAllByUserId(principal.getId());

        System.out.println("IS USERGAME PRESENT?： " + optionalUserGames.isPresent());
        System.out.println("LOGGED IN USER EMAIL: " + principal.getEmail());

        if(optionalUserGames.isPresent()) {

            Set<UserGame> userGames = optionalUserGames.get();

            for (UserGame userGame : userGames) {
                System.out.println("USERGAME ID: " + userGame.getId());
            }

            return optionalUserGames;
        }

        return Optional.empty();
    }
}
