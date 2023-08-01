package com.game.gamelist.service.impl;

import com.game.gamelist.entity.Game;
import com.game.gamelist.entity.User;
import com.game.gamelist.entity.UserGame;
import com.game.gamelist.repository.GameRepository;
import com.game.gamelist.repository.UserGameRepository;
import com.game.gamelist.service.UserGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserGameServiceImpl implements UserGameService {
    private final UserGameRepository userGameRepository;
    private final GameRepository gameRepository;

    @Override
    public Optional<UserGame> findUserGameById(Long requestedId) {
        return userGameRepository.findById(requestedId);
    }


    @Override
    public UserGame createUserGame(UserGame userGame, User principal) {
        Game game = gameRepository.findById(userGame.getGame().getId()).orElse(null);
        if (principal != null && game != null) {
            userGame.setUser(principal);
            userGame.setGame(game);
            return userGameRepository.save(userGame);
        }

        return null;

    }
}
