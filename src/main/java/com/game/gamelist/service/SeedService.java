package com.game.gamelist.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.gamelist.entity.Game;
import com.game.gamelist.entity.User;
import com.game.gamelist.repository.UserRepository;
import com.game.gamelist.repository.GameRepository;
import com.game.gamelist.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;
@Service
public class SeedService {

    private final UserRepository userRepository;
    @Autowired
    public SeedService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public void seedUsersIfEmpty() {
        if (userRepository.count() == 0) {
            ObjectMapper objectMapper = new ObjectMapper();

            try {
                InputStream inputStream = getClass().getResourceAsStream("/json/users.json");
                List<User> users = objectMapper.readValue(inputStream, objectMapper.getTypeFactory().constructCollectionType(List.class, User.class));
                userRepository.saveAll(users);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
