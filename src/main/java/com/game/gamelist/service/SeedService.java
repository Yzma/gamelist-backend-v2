package com.game.gamelist.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.gamelist.entity.*;
import com.game.gamelist.repository.*;
import com.game.gamelist.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.io.InputStream;
import java.util.List;
@Service
public class SeedService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private TagRepository tagRepository;
    @Autowired
    private PlatformRepository platformRepository;

    @PostConstruct
    public void seedUsersIfEmpty() {
        if (userRepository.count() == 0 ) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                System.out.println("🐷🐷🐷🐷🐷🐷🐷🐷🐷🐷🐷 Seeding users...");

                InputStream inputStream = getClass().getResourceAsStream("/json/users.json");
                List<User> users = objectMapper.readValue(inputStream, new TypeReference<List<User>>() {
                });
                userRepository.saveAll(users);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @PostConstruct
    public void seedGamesIfEmpty() {
        if (gameRepository.count() == 0) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                System.out.println("🐷🐷🐷🐷🐷🐷🐷🐷🐷🐷🐷 Seeding games...");

                InputStream inputStream = getClass().getResourceAsStream("/json/games.json");
                List<Game> games = objectMapper.readValue(inputStream, new TypeReference<List<Game>>() {});
                gameRepository.saveAll(games);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @PostConstruct
    public void seedGenresIfEmpty() {
        if (genreRepository.count() == 0) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                InputStream inputStream = getClass().getResourceAsStream("/json/genres.json");
                List<Genre> genres = objectMapper.readValue(inputStream, new TypeReference<List<Genre>>() {
                });
                genreRepository.saveAll(genres);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @PostConstruct
    public void seedTagsIfEmpty() {
        if (tagRepository.count() == 0) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                InputStream inputStream = getClass().getResourceAsStream("/json/tags.json");
                List<Tag> tags = objectMapper.readValue(inputStream, new TypeReference<List<Tag>>() {
                });
                tagRepository.saveAll(tags);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @PostConstruct
    public void seedPlatformsIfEmpty() {
        if (platformRepository.count() == 0) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                InputStream inputStream = getClass().getResourceAsStream("/json/platforms.json");
                List<Platform> platforms = objectMapper.readValue(inputStream, new TypeReference<List<Platform>>() {
                });
                platformRepository.saveAll(platforms);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }


}


