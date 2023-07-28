package com.game.gamelist.service.impl;

import com.game.gamelist.entity.UserGame;
import com.game.gamelist.repository.UserGameRepository;
import com.game.gamelist.service.UserGameService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserGameServiceImpl implements UserGameService {
    private final UserGameRepository userGameRepository;

}
