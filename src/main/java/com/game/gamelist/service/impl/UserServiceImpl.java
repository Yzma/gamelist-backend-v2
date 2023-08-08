package com.game.gamelist.service.impl;

import com.game.gamelist.entity.User;
import com.game.gamelist.exception.UserNotFoundException;
import com.game.gamelist.repository.UserRepository;
import com.game.gamelist.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public Map<String, Object> getUser(long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found. Id: " + id));

        // Extract the required properties and return as a map
        return Map.of(
                "email", user.getEmail(),
                "username", user.getUsername(),
                "bannerPicture", user.getBannerPicture(),
                "userPicture", user.getUserPicture()
        );
    }
}
