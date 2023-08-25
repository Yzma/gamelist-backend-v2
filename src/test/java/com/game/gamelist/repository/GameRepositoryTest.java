package com.game.gamelist.repository;

import com.game.gamelist.config.ContainersEnvironment;
import com.game.gamelist.entity.Game;
import com.game.gamelist.entity.Genre;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

            Game game1 = new Game();
            game1.setName("game1");
            game1.setDescription("game1 description");
            game1.setReleaseDate(LocalDateTime.now());
            game1.addGenre(genreFromDB);
            game1.addGenre(genre2FromDB);

            game1.setUpdatedAt(LocalDateTime.now());
            game1.setCreatedAt(LocalDateTime.now());
            game1.setAvgScore(5);
            game1.setTotalRating(55);
            gameRepository.save(game1);
        }

        @Test
        @Order(1)
        @Transactional
        public void whenFindAll_Expect_ListWithOneGame() {
            List<Game> gameListInit = gameRepository.findAll();
            Genre genre = genreRepository.findByName("genre1");
            assertEquals(1, gameListInit.size());

            Game game = gameListInit.get(0);
            assertEquals("game1", game.getName());
            assertEquals("game1 description", game.getDescription());
            assertEquals(5, game.getAvgScore());
            assertEquals(55, game.getTotalRating());
            assertEquals(2, game.getGenres().size());
            assertEquals(true, game.getGenres().contains(genre));
        }
    }
}
