package com.game.gamelist.repository;


import com.game.gamelist.config.ContainersEnvironment;
import com.game.gamelist.entity.*;
import jakarta.transaction.Transactional;
import net.minidev.json.JSONUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
public class UserGameRepositoryTests extends ContainersEnvironment {

    @Autowired
    private UserGameRepository userGameRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GameRepository gameRepository;


    @Test
    void loadContext() {
        assertNotEquals(null, userGameRepository);
        assertNotEquals(null, userRepository);
    }



    @Test
    @Transactional
    public void when_findAll_ShouldReturn_EmptyList() {
        List<UserGame> userGameList = userGameRepository.findAll();
        Assertions.assertEquals(0, userGameList.size());
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class UserGameRepositoryCRUDTests {

        private User owner;
        private Game game1;
        private Game game2;
        private Game game3;

        @BeforeEach
        void beforeEachTest() {
            owner = User.builder().username("testuser").password("testuser").email("testuser@gmail.com").userPicture("testuser").bio("testuser").bannerPicture("testuser").isActive(true).roles(Set.of(Role.ROLE_USER)).build();

            userRepository.save(owner);

            game1 = Game.builder().name("testgame").description("testgame").imageURL("testImage").bannerURL("testGameBanner").build();

            game2 = Game.builder().name("testgame2").description("testgame2").imageURL("testImage").build();
            game3 = Game.builder().name("testgame3").description("testgame3").imageURL("testImage").build();

            gameRepository.save(game1);
            gameRepository.save(game2);
            gameRepository.save(game3);


            User savedOwner = userRepository.findByEmail(owner.getEmail()).get();

            UserGame userGame1 = UserGame.builder().gameNote("testNote").rating(5).gameStatus(GameStatus.Paused).user(owner).game(game1).build();

            userGameRepository.save(userGame1);

        }

        @Test
        @Transactional
        @Order(1)
        public void when_findAll_ShouldReturn_ListWithOneElement() {
            List<UserGame> userGameList = userGameRepository.findAll();
            Assertions.assertEquals(1, userGameList.size());
        }

        @Test
        @Transactional
        @Order(2)
        public void when_findById_ShouldReturn_UserGame() {


            Set<UserGame> userGamesSet = (userGameRepository.findAllByUserId(owner.getId()).get());

            UserGame onlyUserGame = userGamesSet.stream().findFirst().orElse(null);

            Assertions.assertEquals("testNote", onlyUserGame.getGameNote());

            UserGame userGame = userGameRepository.findById(onlyUserGame.getId()).get();
            Assertions.assertEquals(onlyUserGame.getUser().getId(), userGame.getUser().getId());
            Assertions.assertEquals(GameStatus.Paused, userGame.getGameStatus());
        }

        @Test
        @Transactional
        @Order(3)
        public void when_deleteById_ShouldReturn_EmptyList() {                  Set<UserGame> userGamesSet = (userGameRepository.findAllByUserId(owner.getId()).get());
            UserGame onlyUserGame = userGamesSet.stream().findFirst().orElse(null);

            Assertions.assertEquals("testNote", onlyUserGame.getGameNote());

            userGameRepository.deleteById(onlyUserGame.getId());
            List<UserGame> userGameList = userGameRepository.findAll();
            Assertions.assertEquals(0, userGameList.size());
        }

        @Test
        @Transactional
        @Order(4)
        public void when_deleteAll_ShouldReturn_EmptyList() {
            userGameRepository.deleteAll();
            List<UserGame> userGameList = userGameRepository.findAll();
            Assertions.assertEquals(0, userGameList.size());
        }

        @Test
        @Transactional
        @Order(5)
        public void when_update_ShouldReturn_UpdatedUserGame() {
            Set<UserGame> userGamesSet = (userGameRepository.findAllByUserId(owner.getId()).get());
            UserGame onlyUserGame = userGamesSet.stream().findFirst().orElse(null);

            Assertions.assertEquals("testNote", onlyUserGame.getGameNote());

            onlyUserGame.setGameStatus(GameStatus.Completed);
            onlyUserGame.setRating(4);
            onlyUserGame.setGameNote("updatedNote");

            userGameRepository.save(onlyUserGame);

            UserGame updatedUserGame = userGameRepository.findById(onlyUserGame.getId()).get();

            Assertions.assertEquals(GameStatus.Completed, updatedUserGame.getGameStatus());
            Assertions.assertEquals(4, updatedUserGame.getRating());
            Assertions.assertEquals("updatedNote", updatedUserGame.getGameNote());
        }
    }

}
