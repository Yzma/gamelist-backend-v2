package com.game.gamelist.repository;

import com.game.gamelist.config.ContainersEnvironment;
import com.game.gamelist.entity.*;
import com.game.gamelist.entity.Tag;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
public class GameRepositoryTest extends ContainersEnvironment {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private PlatformRepository platformRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserGameRepository userGameRepository;

    @Autowired
    private LikeRepository likeRepository;

    @Test
    public void whenFindAll_Expect_EmptyList() {
        List<Game> gameList = gameRepository.findAll();
        assertEquals(0, gameList.size());
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class GameRepositoryCRUDTests {
        @BeforeEach
        void beforeEachTest() {
            Genre genre = new Genre();
            genre.setName("genre1");
            genreRepository.save(genre);
            Genre genreFromDB = genreRepository.findByName("genre1");

            Genre genre2 = new Genre();
            genre2.setName("genre2");
            genreRepository.save(genre2);
            Genre genre2FromDB = genreRepository.findByName("genre2");

            Platform platform = new Platform();
            platform.setId(1L);
            platform.setName("platform1");
            platformRepository.save(platform);
            Platform platformFromDB = platformRepository.findByName("platform1");

            Platform platform2 = new Platform();
            platform2.setId(2L);
            platform2.setName("platform2");
            platformRepository.save(platform2);
            Platform platform2FromDB = platformRepository.findByName("platform2");

            Tag tag = new Tag();
            tag.setName("tag1");
            tagRepository.save(tag);
            Tag tagFromDB = tagRepository.findByName("tag1");

            Tag tag2 = new Tag();
            tag2.setName("tag2");
            tagRepository.save(tag2);
            Tag tag2FromDB = tagRepository.findByName("tag2");

            Game game1 = new Game();
            game1.setName("game1");
            game1.setDescription("game1 description");
            game1.setReleaseDate(LocalDateTime.now());
            game1.setGenres(Set.of(genreFromDB, genre2FromDB));
            game1.setPlatforms(Set.of(platformFromDB, platform2FromDB));
            game1.setTags(Set.of(tagFromDB, tag2FromDB));
            game1.setUpdatedAt(LocalDateTime.now());
            game1.setCreatedAt(LocalDateTime.now());
            game1.setAvgScore(5);
            game1.setTotalRating(55);
            gameRepository.save(game1);
        }

        @Test
        @Order(1)
        @Transactional
        public void when_FindAll_Expect_ListWithOneGame() {
            List<Game> gameListInit = gameRepository.findAll();
            Genre genre = genreRepository.findByName("genre1");
            Platform platform = platformRepository.findByName("platform1");
            Tag tag = tagRepository.findByName("tag1");
            assertEquals(1, gameListInit.size());

            Game game = gameListInit.get(0);
            assertEquals("game1", game.getName());
            assertEquals("game1 description", game.getDescription());
            assertEquals(5, game.getAvgScore());
            assertEquals(55, game.getTotalRating());
            assertEquals(2, game.getGenres().size());
            assertEquals(2, game.getPlatforms().size());
            assertEquals(2, game.getTags().size());
            assertTrue(game.getGenres().contains(genre));
            assertTrue(game.getGenres().contains(genre));
        }

        @Test
        @Order(2)
        @Transactional
        public void when_findAllGamesOrderedById_Expect_ListOfGamesOrderedById() {
            List<Game> gameList = new ArrayList<>();

            for (int i = 0; i < 6; i++) {
                Game game = new Game();
                game.setName("game" + i);
                game.setDescription("game" + i + " description");
                game.setReleaseDate(LocalDateTime.now());
                game.setUpdatedAt(LocalDateTime.now());
                game.setCreatedAt(LocalDateTime.now());
                game.setAvgScore(5 + i);
                game.setTotalRating(55 + i);
                gameList.add(game);
            }

            gameRepository.saveAll(gameList);

            List<Game> gameListInit = gameRepository.findAllGamesOrderedById();
            assertEquals(7, gameListInit.size());
            assertEquals("game1", gameListInit.get(0).getName());
            assertEquals("game0", gameListInit.get(1).getName());
            assertEquals("game1", gameListInit.get(2).getName());
            assertEquals("game2", gameListInit.get(3).getName());
            assertTrue(gameListInit.get(1).getAvgScore() < gameListInit.get(5).getAvgScore());
        }
    }

    @Test
    @Order(3)
    @Transactional
    public void when_findGamesByUserIdAndStatus_returnListOfGames_byStatus() {
        List<Game> gameList = new ArrayList<>();
        User owner = User.builder().username("owner").password("owner").email("owner@gmail.com").build();
        userRepository.save(owner);
        List<UserGame> userGameList = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            Game game = new Game();
            game.setName("game" + i);
            game.setDescription("game" + i + " description");
            game.setReleaseDate(LocalDateTime.now());
            game.setUpdatedAt(LocalDateTime.now());
            game.setCreatedAt(LocalDateTime.now());
            game.setAvgScore(5 + i);
            game.setTotalRating(55 + i);
            gameList.add(game);

            UserGame userGame = new UserGame();
            userGame.setGame(game);
            userGame.setUser(owner);
            userGame.setGameStatus(GameStatus.values()[i]);
            userGame.setGameNote("note" + i);
            userGameList.add(userGame);
        }

        gameRepository.saveAll(gameList);
        userGameRepository.saveAll(userGameList);

        List<Game> gameListInit = gameRepository.findAllGamesOrderedById();
        assertEquals(6, gameListInit.size());
        assertEquals("game0", gameListInit.get(0).getName());
        assertEquals("game1", gameListInit.get(1).getName());
        assertEquals("game2", gameListInit.get(2).getName());
        assertEquals("game3", gameListInit.get(3).getName());
        assertTrue(gameListInit.get(1).getAvgScore() < gameListInit.get(5).getAvgScore());

        List<Game> gameListByStatus = gameRepository.findGamesByUserIdAndStatus(owner.getId(), GameStatus.Playing);
        assertEquals(1, gameListByStatus.size());
        assertEquals("game0", gameListByStatus.get(0).getName());
    }

    @Test
    @Order(4)
    @Transactional
    public void when_getFurthestYear_returnYearInt() {
        List<Game> gameList = new ArrayList<>();

        for (int i = 0; i < 6; i++) {
            Game game = new Game();
            game.setName("game" + i);
            game.setDescription("game" + i + " description");
            game.setReleaseDate(LocalDateTime.of(2021 - i, 1, 1, 1, 1, 1));
            game.setUpdatedAt(LocalDateTime.now());
            game.setCreatedAt(LocalDateTime.now());
            game.setAvgScore(5 + i);
            game.setTotalRating(55 + i);
            gameList.add(game);
        }

        gameRepository.saveAll(gameList);
        int furthestYear = gameRepository.getFurthestYear();
        assertEquals(2021, furthestYear);
    }

    @Test
    @Order(5)
    @Transactional
    public void when_existsByUserIdAndGameId_returnTrue() {
        User user = User.builder().username("user").password("user").email("user@gmail.com").build();
        userRepository.save(user);
        Game game = Game.builder().name("game").description("game description").build();
        gameRepository.save(game);
        LikeEntity likeEntity = LikeEntity.builder().user(user).interactiveEntity(game).build();
        likeRepository.save(likeEntity);

        boolean isGameLiked = likeRepository.existsByUserIdAndInteractiveEntityId(user.getId(), game.getId());

        assertTrue(isGameLiked);

    }
}
