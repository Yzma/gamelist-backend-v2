package com.game.gamelist.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.gamelist.entity.*;
import com.game.gamelist.repository.*;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
@Profile("dev")
@RequiredArgsConstructor
public class SeedService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final GameRepository gameRepository;
    private final GenreRepository genreRepository;
    private final TagRepository tagRepository;
    private final PlatformRepository platformRepository;
    private final UserGameRepository userGameRepository;
    private final GameJournalRepository gameJournalRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @PostConstruct
    @Transactional
    public void seedDatabase() {
        seedPlatformsIfEmpty();
        seedGenresIfEmpty();
        seedTagsIfEmpty();
        seedUsersIfEmpty();
        seedGamesIfEmpty();
        seedPostsIfEmpty();
        seedUserGamesIfEmpty();
        seedGameJournalsIfEmpty();
    }
    public void seedUsersIfEmpty() {
        if (userRepository.count() == 0) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {

                InputStream inputStream = getClass().getResourceAsStream("/json/users.json");
                List<User> users = objectMapper.readValue(inputStream, new TypeReference<List<User>>() {
                });
                userRepository.saveAll(users);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional
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
    @Transactional
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
    @Transactional
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


    @Transactional
    public void seedGamesIfEmpty() {
        if (gameRepository.count() == 0) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {

                InputStream inputStream = getClass().getResourceAsStream("/json/games.json");
                List<JsonNode> gameNodes = objectMapper.readValue(inputStream, new TypeReference<List<JsonNode>>() {
                });

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

                    List<Genre> genres = new ArrayList<>();
                    JsonNode genresNode = gameNode.get("genres");
                    for (JsonNode genreNode : genresNode) {
                        Genre genre = genreRepository.findByName(genreNode.asText());
                        genres.add(genre);
                    }
                    game.setGenres(new HashSet<>(genres));

                    List<Tag> tags = new ArrayList<>();
                    JsonNode tagsNode = gameNode.get("themes");
                    for (JsonNode tagNode : tagsNode) {
                        Tag tag = tagRepository.findByName(tagNode.asText());
                        if (tag == null) {
                            tag = new Tag();
                            tag.setName(tagNode.asText());
                            tagRepository.save(tag);
                        }
                        tags.add(tag);
                    }
                    game.setTags(new HashSet<>(tags));

//                    JsonNode platformsNode = gameNode.get("platforms");
//                    for (JsonNode platformNode : platformsNode) {
//                        Platform platform = platformRepository.findByName(platformNode.asText());
//                        if (platform == null) {
//                            platform = new Platform();
//                            platform.setName(platformNode.asText());
//                            platformRepository.save(platform);
//                        }
//                        game.addPlatform(platform);
//                    }

                    List<Platform> platforms = new ArrayList<>();
                    JsonNode platformsNode = gameNode.get("platforms");
                    for (JsonNode platformNode : platformsNode) {
                        Platform platform = platformRepository.findByName(platformNode.asText());
                        if (platform == null) {
                            platform = new Platform();
                            platform.setName(platformNode.asText());
                            platformRepository.save(platform);
                        }
                        platforms.add(platform);
                    }
                    game.setPlatforms(new HashSet<>(platforms));
                    
                    game.setCreatedAt(LocalDateTime.now());
                    game.setUpdatedAt(LocalDateTime.now());

                    games.add(game);
                }

                gameRepository.saveAll(games);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    @Transactional
    public void seedGameJournalsIfEmpty() {
        if (gameJournalRepository.count() == 0) {
            for (int i = 1; i < 4; i++) {
                User user = userRepository.findById(Long.valueOf(i)).get();
                List<GameJournal> gameJournals = new ArrayList<>();
                for (int j = 1; j < 6; j++) {
                    try {

                        GameJournal gameJournal = new GameJournal();
                        gameJournal.setContent("This is the body of game journal " + j + " by user " + i + " .");
                        gameJournal.setCreatedAt(LocalDateTime.now());
                        gameJournal.setUpdatedAt(LocalDateTime.now());
                        gameJournal.setUser(user);

                        gameJournals.add(gameJournal);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                gameJournalRepository.saveAll(gameJournals);
            }
        }
    }
    @Transactional
    public void seedPostsIfEmpty() {
        if (postRepository.count() == 0) {

            for (int i = 1; i < 4; i++) {
                User user = userRepository.findById(Long.valueOf(i)).get();
                List<Post> posts = new ArrayList<>();
                for (int j = 1; j < 6; j++) {
                    try {

                        Post post = new Post();
                        post.setText("This is a post " + j + " by user " + i + " .");
                        post.setCreatedAt(LocalDateTime.now());
                        post.setUpdatedAt(LocalDateTime.now());
                        post.setUser(user);

                        posts.add(post);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                postRepository.saveAll(posts);
            }

        }
    }
    @Transactional
    public void seedUserGamesIfEmpty() {
        if (userGameRepository.count() == 0) {
            for (int i = 1; i < 4; i++) {
                User user = userRepository.findById(Long.valueOf(i)).get();
                List<UserGame> userGames = new ArrayList<>();
                for (int j = 1; j < 6; j++) {
                    try {

                        UserGame userGame = new UserGame();
                        userGame.setCreatedAt(LocalDateTime.now());
                        userGame.setUpdatedAt(LocalDateTime.now());
                        userGame.setGameStatus(GameStatus.values()[j % 3]);
                        userGame.setIsPrivate(false);
                        userGame.setUser(user);
                        userGame.setGame(gameRepository.findAllGamesOrderedById().get(j - 1));
                        userGame.setRating(5);
                        userGame.setGameNote("This is a review for game " + j + " by user " + i + " .");

                        userGames.add(userGame);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                userGameRepository.saveAll(userGames);
            }
        }
    }
}
