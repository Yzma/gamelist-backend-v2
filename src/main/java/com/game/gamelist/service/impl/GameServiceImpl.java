package com.game.gamelist.service.impl;

import com.game.gamelist.entity.Game;
import com.game.gamelist.repository.GameRepository;
import com.game.gamelist.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private final GameRepository gameRepository;

    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }
}
