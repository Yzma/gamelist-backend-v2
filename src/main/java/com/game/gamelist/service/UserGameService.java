package com.game.gamelist.service;

import com.game.gamelist.dto.UserGamesSummaryDTO;
import com.game.gamelist.entity.User;
import com.game.gamelist.entity.UserGame;
import com.game.gamelist.model.EditUserGameRequest;

import java.util.Set;


public interface UserGameService {
    UserGame findUserGameById(Long requestedId, User principal);

    UserGame createUserGame(EditUserGameRequest userGame, User principal);

    UserGame updateUserGameById(EditUserGameRequest userGame, User principal);

    UserGame deleteUserGameById(Long requestedId, User principal);

    Set<UserGame> findAllUserGamesByUserId(User principal);

    UserGamesSummaryDTO findAllUserGamesByUserIdByStatus(User principal);

    UserGame findUserGameByGameId(Long gameId, User principal);

    UserGame deleteUserGameByGameId(Long gameId, User principal);
}
