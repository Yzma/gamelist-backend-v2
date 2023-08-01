package com.game.gamelist.service.impl;

import com.game.gamelist.entity.User;
import com.game.gamelist.repository.UserRepository;
import com.game.gamelist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
