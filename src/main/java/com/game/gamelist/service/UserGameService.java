package com.game.gamelist.service;

import com.game.gamelist.entity.User;
import com.game.gamelist.entity.UserGame;
import com.game.gamelist.repository.UserGameRepository;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;


public interface UserGameService {
    Optional<UserGame> findUserGameById(Long requestedId);

    UserGame createUserGame(UserGame userGame, User principal);

}
