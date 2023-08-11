package com.game.gamelist.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
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
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        System.out.println("�������Seeding users...");
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
    public void seedGenresIfEmpty() {
        System.out.println("�������Seeding genres...");
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
        System.out.println("�������Seeding tags...");
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

        System.out.println("�������Seeding platforms...");
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
    @PostConstruct
    public void seedGamesIfEmpty() {
        System.out.println("�������Seeding games...");
        if (gameRepository.count() == 0) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                System.out.println("🐷🐷🐷🐷🐷🐷🐷🐷🐷🐷🐷 Seeding games...");

                InputStream inputStream = getClass().getResourceAsStream("/json/games.json");
                List<JsonNode> gameNodes = objectMapper.readValue(inputStream, new TypeReference<List<JsonNode>>() {});

                List<Game> games = new ArrayList<>();

                for (JsonNode gameNode : gameNodes) {
                    Game game = new Game();
                    game.setId(gameNode.get("id").asLong());
                    game.setName(gameNode.get("name").asText());
                    game.setDescription(gameNode.get("summary").asText());
                    game.setImageURL("https:" + gameNode.get("cover").asText());
                    game.setReleaseDate(LocalDateTime.ofInstant(Instant.ofEpochSecond(gameNode.get("first_release_date").asLong()), ZoneId.systemDefault()));
                    game.setAvgScore(Math.round(gameNode.get("total_rating").asDouble() * 10.0) / 10.0);
                    game.setTotalRating(gameNode.get("total_rating_count").asInt());
                    game.setBannerURL("https:" + gameNode.get("screenshots").get(0).asText());

                    Set<String> genreNames = new HashSet<>(gameNode.get("genres").findValuesAsText("name"));

                    for (String genreName : genreNames) {
                        Genre genre = genreRepository.findByName(genreName);
                        game.getGenres().add(genre);
                    }

                    Set<String> tagNames = new HashSet<>(gameNode.get("themes").findValuesAsText("name"));

                    for (String tagName : tagNames) {
                        Tag tag = tagRepository.findByName(tagName);
                        game.getTags().add(tag);
                    }

                    Set<String> platformNames = new HashSet<>(gameNode.get("platforms").findValuesAsText("name"));

                    for (String platformName : platformNames) {
                        Platform platform = platformRepository.findByName(platformName);
                        game.getPlatforms().add(platform);
                    }
                    game.setCreatedAt(LocalDateTime.now());
                    game.setUpdatedAt(LocalDateTime.now());

                    games.add(game);
                }

                gameRepository.saveAll(games);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}


