package com.game.gamelist.service.impl;

import com.game.gamelist.entity.User;
import com.game.gamelist.exception.UserNotFoundException;
import com.game.gamelist.repository.UserRepository;
import com.game.gamelist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public User getUser(long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User not found. Id: " + id));
    }
}
