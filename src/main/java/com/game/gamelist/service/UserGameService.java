package com.game.gamelist.service;

import com.game.gamelist.entity.User;
import com.game.gamelist.entity.UserGame;
import com.game.gamelist.repository.UserGameRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.Set;


public interface UserGameService {
    UserGame findUserGameById(Long requestedId, User principal);

    UserGame createUserGame(UserGame userGame, User principal);

    Optional<UserGame> updateUserGameById(Long requestedId, UserGame userGame, User principal);

    Optional<UserGame> deleteUserGameById(Long requestedId, User principal);

    Optional<Set<UserGame>> findAllUserGamesByUserId(User principal);

}
