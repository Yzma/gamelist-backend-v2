package com.game.gamelist.service;

import com.game.gamelist.config.ContainersEnvironment;
import com.game.gamelist.dto.GameDTO;
import com.game.gamelist.entity.Game;
import com.game.gamelist.entity.Genre;
import com.game.gamelist.entity.Platform;
import com.game.gamelist.entity.Tag;
import com.game.gamelist.model.GameQueryFilters;
import com.game.gamelist.repository.GameRepository;
import com.game.gamelist.repository.GenreRepository;
import com.game.gamelist.repository.PlatformRepository;
import com.game.gamelist.repository.TagRepository;
import com.game.gamelist.service.impl.GameServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
public class GameServiceTests extends ContainersEnvironment {

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private PlatformRepository platformRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private GameServiceImpl gameService;

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class GameServiceCRUDTests {

        @BeforeEach
        void beforeEachTest() {
            // Genres
            Genre actionGenre = new Genre();
            actionGenre.setName("Action");

            Genre sportsGenre = new Genre();
            sportsGenre.setName("Sports");

            Genre turnBasedGenre = new Genre();
            turnBasedGenre.setName("Turn-based strategy");

            Genre stealthGenre = new Genre();
            stealthGenre.setName("Stealth");

            Genre rpgGenre = new Genre();
            rpgGenre.setName("Role-playing");

            genreRepository.saveAll(Arrays.asList(actionGenre, sportsGenre, turnBasedGenre, stealthGenre, rpgGenre));

            // Tags
            Tag multiplayerTag = new Tag();
            multiplayerTag.setName("Multiplayer");

            Tag singleplayerTag = new Tag();
            singleplayerTag.setName("Singleplayer");

            Tag threeDTag = new Tag();
            threeDTag.setName("3D");

            Tag fantasyTag = new Tag();
            fantasyTag.setName("Fantasy");

            Tag sandboxTag = new Tag();
            sandboxTag.setName("Sandbox");

            tagRepository.saveAll(Arrays.asList(multiplayerTag, singleplayerTag, threeDTag, fantasyTag, sandboxTag));

            // Platforms
            Platform playstation5Platform = new Platform();
            playstation5Platform.setName("Playstation 5");

            Platform xboxOnePlatform = new Platform();
            xboxOnePlatform.setName("Xbox One");

            Platform windowsPlatform = new Platform();
            windowsPlatform.setName("Windows");

            Platform nintendoSwitchPlatform = new Platform();
            nintendoSwitchPlatform.setName("Nintendo Switch");

            Platform macOSPlatform = new Platform();
            macOSPlatform.setName("macOS");

            platformRepository.saveAll(Arrays.asList(playstation5Platform, xboxOnePlatform, windowsPlatform, nintendoSwitchPlatform, macOSPlatform));

            // Games
            Game persona5 = new Game();
            persona5.setName("Persona 5");
            persona5.setDescription("Inside a casino, during one of their heists, the group known as Phantom Thieves of Hearts is cornered by the police. Unable to escape, the leader of the group (the game's protagonist) is put under arrest, and goes into interrogation.");
            persona5.setImageURL("https://www.mobygames.com/game/86408/persona-5/");
            persona5.setBannerURL("https://images.igdb.com/igdb/image/upload/t_screenshot_big/gin55k9eyfq8udk0taym.jpg");
            persona5.setReleaseDate(LocalDateTime.of(2014, 9, 23, 0, 0));
            persona5.setAvgScore(4.5);
            persona5.setTotalRating(900);
            persona5.setGenres(Set.of(actionGenre, turnBasedGenre, rpgGenre));
            persona5.setPlatforms(Set.of(playstation5Platform, xboxOnePlatform, windowsPlatform, nintendoSwitchPlatform));
            persona5.setTags(Set.of(singleplayerTag, threeDTag, fantasyTag));

            Game fifa11 = new Game();
            fifa11.setName("FIFA 11");
            fifa11.setDescription("FIFA Soccer 11 is a licensed soccer game which allows the player to directly control his athletes. He has various playing modes to his disposal, e.g. single matches, multiplayer or leagues to prove his skill.");
            fifa11.setImageURL("https://www.mobygames.com/game/51960/fifa-soccer-11/");
            fifa11.setBannerURL("https://images.igdb.com/igdb/image/upload/t_screenshot_big/gin55k9eyfq8udk0taym.jpg");
            fifa11.setReleaseDate(LocalDateTime.of(2011, 9, 23, 0, 0));
            fifa11.setAvgScore(4.7);
            fifa11.setTotalRating(2000);
            fifa11.setGenres(Set.of(sportsGenre));
            fifa11.setPlatforms(Set.of(playstation5Platform, xboxOnePlatform, windowsPlatform));
            fifa11.setTags(Set.of(multiplayerTag, threeDTag));

            Game assassinsCreed = new Game();
            assassinsCreed.setName("Assassin's Creed");
            assassinsCreed.setDescription("Assassin's Creed is a non-linear action-adventure video game, during which the player controls a 12th-century Levantine Assassin named Ibn-La'Ahad during the Third Crusade, whose life is experienced through the Animus by his 21st century descendant, Desmond Miles.");
            assassinsCreed.setImageURL("https://www.igdb.com/games/assassin-s-creed/");
            assassinsCreed.setBannerURL("https://images.igdb.com/igdb/image/upload/t_screenshot_big/gin55k9eyfq8udk0taym.jpg");
            assassinsCreed.setReleaseDate(LocalDateTime.of(2007, 9, 23, 0, 0));
            assassinsCreed.setAvgScore(4.4);
            assassinsCreed.setTotalRating(3000);
            assassinsCreed.setGenres(Set.of(actionGenre, stealthGenre, rpgGenre));
            assassinsCreed.setPlatforms(Set.of(playstation5Platform, xboxOnePlatform, windowsPlatform));
            assassinsCreed.setTags(Set.of(singleplayerTag, threeDTag, fantasyTag));

            Game darkSouls1 = new Game();
            darkSouls1.setName("Dark Souls: Remastered");
            darkSouls1.setDescription("");
            darkSouls1.setImageURL("https://images.igdb.com/igdb/image/upload/t_cover_big/co2uro.png");
            darkSouls1.setBannerURL("https://images.igdb.com/igdb/image/upload/t_screenshot_big/zqpwusikqtoeawmsdaqd.jpg");
            darkSouls1.setReleaseDate(LocalDateTime.of(2014, 5, 25, 0, 0));
            darkSouls1.setAvgScore(8);
            darkSouls1.setTotalRating(1000);
            darkSouls1.setGenres(Set.of(actionGenre, rpgGenre));
            darkSouls1.setPlatforms(Set.of(playstation5Platform, xboxOnePlatform, windowsPlatform));
            darkSouls1.setTags(Set.of(singleplayerTag, threeDTag, fantasyTag));

            Game legendOfZelda = new Game();
            legendOfZelda.setName("The Legend of Zelda: Breath of the Wild");
            legendOfZelda.setDescription("");
            legendOfZelda.setImageURL("https://images.igdb.com/igdb/image/upload/t_cover_big/co3p2d.png");
            legendOfZelda.setBannerURL("https://images.igdb.com/igdb/image/upload/t_screenshot_big/sckj6g.jpg");
            legendOfZelda.setReleaseDate(LocalDateTime.of(2017, 3, 3, 0, 0));
            legendOfZelda.setAvgScore(9.5);
            legendOfZelda.setTotalRating(690);
            legendOfZelda.setGenres(Set.of(actionGenre, rpgGenre));
            legendOfZelda.setPlatforms(Set.of(nintendoSwitchPlatform));
            legendOfZelda.setTags(Set.of(singleplayerTag, threeDTag, fantasyTag));

            Game darkSouls2 = new Game();
            darkSouls2.setName("Dark Souls II Scholar of the First Sin");
            darkSouls2.setDescription("");
            darkSouls2.setImageURL("https://images.igdb.com/igdb/image/upload/t_cover_big/co20um.png");
            darkSouls2.setBannerURL("https://images.igdb.com/igdb/image/upload/t_screenshot_big/g8roegct3qyzmnubhoeb.jpg");
            darkSouls2.setReleaseDate(LocalDateTime.of(2015, 4, 2, 0, 0));
            darkSouls2.setAvgScore(8.5);
            darkSouls2.setTotalRating(850);
            darkSouls2.setGenres(Set.of(actionGenre, rpgGenre));
            darkSouls2.setPlatforms(Set.of(playstation5Platform, xboxOnePlatform, windowsPlatform));
            darkSouls2.setTags(Set.of(singleplayerTag, threeDTag, fantasyTag));

            Game rocketLeague = new Game();
            rocketLeague.setName("Rocket League");
            rocketLeague.setDescription("Rocket League is a high-powered hybrid of arcade-style soccer and vehicular mayhem with easy-to-understand controls and fluid, physics-driven competition. Rocket League includes casual and competitive Online Matches, a fully-featured offline Season Mode, special 'Mutators' that let you change the rules entirely, hockey and basketball-inspired Extra Modes, and more than 500 trillion possible cosmetic customization combinations.");
            rocketLeague.setImageURL("https://images.igdb.com/igdb/image/upload/t_cover_big/co5w0w.png");
            rocketLeague.setBannerURL("https://images.igdb.com/igdb/image/upload/t_screenshot_big/ygepetru87ka9nzqowr6.jpg");
            rocketLeague.setReleaseDate(LocalDateTime.of(2021, 6, 15, 0, 0));
            rocketLeague.setAvgScore(7.1);
            rocketLeague.setTotalRating(700);
            rocketLeague.setGenres(Set.of(actionGenre, sportsGenre));
            rocketLeague.setPlatforms(Set.of(playstation5Platform, xboxOnePlatform, windowsPlatform, nintendoSwitchPlatform, macOSPlatform));
            rocketLeague.setTags(Set.of(multiplayerTag, threeDTag));

            gameRepository.saveAll(Arrays.asList(persona5, fifa11, assassinsCreed, darkSouls1, legendOfZelda, darkSouls2, rocketLeague));
        }

        @Test
        @Transactional
        @Description("Returns the first game in the database when no filters are provided")
        @Order(1)
        public void oneGameWithNoFilters() {
            List<GameDTO> foundGames = gameService.getAllGames(new GameQueryFilters());
            Assertions.assertEquals(1, foundGames.size());
            Assertions.assertEquals("Persona 5", foundGames.get(0).getName());
        }

        @Test
        @Description("Returns all games with limit set")
        @Order(2)
        @Transactional
        public void returnsAllGamesWithLimit() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(7, foundGames.size());
        }

        @Test
        @Transactional
        @Description("Returns all games with 1 genre")
        @Order(3)
        void returnsAllGamesWithOneGenre() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setGenres(List.of("Action"));
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(6, foundGames.size());
            Assertions.assertTrue(foundGames.stream().noneMatch(gameDTO -> gameDTO.getName().equals("FIFA 11")));
        }

        @Test
        @Transactional
        @Description("Returns all games with multiple genres")
        @Order(4)
        void returnsAllGamesWithMultipleGenres() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setGenres(List.of("Action", "Role-playing"));
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(5, foundGames.size());
            Assertions.assertTrue(foundGames.stream().noneMatch(gameDTO -> gameDTO.getName().equals("Rocket League")));
            Assertions.assertTrue(foundGames.stream().noneMatch(gameDTO -> gameDTO.getName().equals("FIFA 11")));
        }

        @Test
        @Transactional
        @Description("Returns all games with 1 tag")
        @Order(5)
        void returnsAllGamesWithOneTag() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setTags(List.of("Multiplayer"));
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(2, foundGames.size());
            Assertions.assertTrue(foundGames.stream().anyMatch(gameDTO -> gameDTO.getName().equals("Rocket League")));
            Assertions.assertTrue(foundGames.stream().anyMatch(gameDTO -> gameDTO.getName().equals("FIFA 11")));
        }

        @Test
        @Transactional
        @Description("Returns all games with multiple tags")
        @Order(6)
        void returnsAllGamesWithMultipleTags() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setTags(List.of("Singleplayer", "3D"));
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(5, foundGames.size());
            Assertions.assertTrue(foundGames.stream().noneMatch(gameDTO -> gameDTO.getName().equals("Rocket League")));
            Assertions.assertTrue(foundGames.stream().noneMatch(gameDTO -> gameDTO.getName().equals("FIFA 11")));
        }

        @Test
        @Transactional
        @Description("Returns all games with 1 platform")
        @Order(7)
        void returnsAllGamesWithOnePlatform() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setPlatforms(List.of("Playstation 5"));
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(6, foundGames.size());
            Assertions.assertTrue(foundGames.stream().noneMatch(gameDTO -> gameDTO.getName().equals("The Legend of Zelda: Breath of the Wild")));
        }

        @Test
        @Transactional
        @Description("Returns all games with multiple platforms")
        @Order(8)
        void returnsAllGamesWithMultiplePlatforms() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setPlatforms(List.of("Playstation 5", "Xbox One"));
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(6, foundGames.size());
            Assertions.assertTrue(foundGames.stream().noneMatch(gameDTO -> gameDTO.getName().equals("The Legend of Zelda: Breath of the Wild")));
        }

        @Test
        @Transactional
        @Description("Returns 0 games when provided invalid tag")
        @Order(9)
        void returnsAllGamesWithInvalidFilters() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setPlatforms(List.of("Not-A-Platform"));
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(0, foundGames.size());
        }

        @Test
        @Transactional
        @Description("Returns 0 games when no games match the provided filters")
        @Order(10)
        void returnsAllGamesWithTooManyFilters() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setGenres(List.of("Action", "Sports", "Turn-based strategy"));
            gameQueryFilters.setPlatforms(List.of("Playstation 5", "Xbox One"));
            gameQueryFilters.setTags(List.of("Multiplayer", "Singleplayer"));
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(0, foundGames.size());
        }

        @Test
        @Transactional
        @Description("Returns correct amount of games when provided 2 genres, platforms and tags")
        @Order(11)
        void returnsCorrectGamesWithManyFilters() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setGenres(List.of("Action", "Role-playing"));
            gameQueryFilters.setPlatforms(List.of("Playstation 5", "Xbox One"));
            gameQueryFilters.setTags(List.of("Singleplayer", "3D"));
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(4, foundGames.size());
            Assertions.assertTrue(foundGames.stream().anyMatch(gameDTO -> gameDTO.getName().equals("Dark Souls II Scholar of the First Sin")));
            Assertions.assertTrue(foundGames.stream().anyMatch(gameDTO -> gameDTO.getName().equals("Dark Souls: Remastered")));
            Assertions.assertTrue(foundGames.stream().anyMatch(gameDTO -> gameDTO.getName().equals("Assassin's Creed")));
            Assertions.assertTrue(foundGames.stream().anyMatch(gameDTO -> gameDTO.getName().equals("Persona 5")));
        }

        @Test
        @Transactional
        @Description("Returns correct amount of games when provided 3 genres")
        @Order(12)
        void returnsCorrectGamesWith3Genres() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setGenres(List.of("Action", "Stealth", "Role-playing"));
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(1, foundGames.size());
            Assertions.assertEquals("Assassin's Creed", foundGames.get(0).getName());
        }

        @Test
        @Transactional
        @Description("Returns correct amount of games when provided 3 platforms")
        @Order(13)
        void returnsCorrectGamesWith3Platforms() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setPlatforms(List.of("Playstation 5", "Xbox One", "Windows"));
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(6, foundGames.size());
            Assertions.assertTrue(foundGames.stream().anyMatch(gameDTO -> gameDTO.getName().equals("Dark Souls II Scholar of the First Sin")));
            Assertions.assertTrue(foundGames.stream().anyMatch(gameDTO -> gameDTO.getName().equals("Dark Souls: Remastered")));
            Assertions.assertTrue(foundGames.stream().anyMatch(gameDTO -> gameDTO.getName().equals("Assassin's Creed")));
            Assertions.assertTrue(foundGames.stream().anyMatch(gameDTO -> gameDTO.getName().equals("Persona 5")));
            Assertions.assertTrue(foundGames.stream().anyMatch(gameDTO -> gameDTO.getName().equals("Rocket League")));
            Assertions.assertTrue(foundGames.stream().anyMatch(gameDTO -> gameDTO.getName().equals("FIFA 11")));
        }

        @Test
        @Transactional
        @Description("Returns correct amount of games when provided 3 Tags")
        @Order(14)
        void returnsCorrectGamesWith3Tags() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setTags(List.of("Singleplayer", "3D", "Fantasy"));
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(5, foundGames.size());
            Assertions.assertTrue(foundGames.stream().anyMatch(gameDTO -> gameDTO.getName().equals("Dark Souls II Scholar of the First Sin")));
            Assertions.assertTrue(foundGames.stream().anyMatch(gameDTO -> gameDTO.getName().equals("Dark Souls: Remastered")));
            Assertions.assertTrue(foundGames.stream().anyMatch(gameDTO -> gameDTO.getName().equals("Assassin's Creed")));
            Assertions.assertTrue(foundGames.stream().anyMatch(gameDTO -> gameDTO.getName().equals("Persona 5")));
            Assertions.assertTrue(foundGames.stream().anyMatch(gameDTO -> gameDTO.getName().equals("The Legend of Zelda: Breath of the Wild")));
        }

        @Test
        @Transactional
        @Description("Returns correct amount of games when provided 2 genres, 3 platforms and 2 tags")
        @Order(15)
        void returnsCorrectGamesWithComplexFilters() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setGenres(List.of("Action", "Role-playing"));
            gameQueryFilters.setPlatforms(List.of("Playstation 5", "Xbox One", "Windows"));
            gameQueryFilters.setTags(List.of("Singleplayer", "3D"));
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(4, foundGames.size());
            Assertions.assertTrue(foundGames.stream().anyMatch(gameDTO -> gameDTO.getName().equals("Dark Souls II Scholar of the First Sin")));
            Assertions.assertTrue(foundGames.stream().anyMatch(gameDTO -> gameDTO.getName().equals("Dark Souls: Remastered")));
            Assertions.assertTrue(foundGames.stream().anyMatch(gameDTO -> gameDTO.getName().equals("Assassin's Creed")));
            Assertions.assertTrue(foundGames.stream().anyMatch(gameDTO -> gameDTO.getName().equals("Persona 5")));
        }

        @Test
        @Transactional
        @Description("Returns all games with 1 exclusion genre")
        @Order(16)
        void returnsAllGamesWithOneExclusionGenre() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setExcludedGenres(List.of("Action"));
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(1, foundGames.size());
            Assertions.assertTrue(foundGames.stream().allMatch(gameDTO -> gameDTO.getName().equals("FIFA 11")));
        }

        @Test
        @Transactional
        @Description("Returns all games with multiple exclusion genres")
        @Order(17)
        void returnsAllGamesWithMultipleExclusionGenres() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setExcludedGenres(List.of("Action", "Role-playing"));
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(1, foundGames.size());
            Assertions.assertTrue(foundGames.stream().allMatch(gameDTO -> gameDTO.getName().equals("FIFA 11")));
        }

        @Test
        @Transactional
        @Description("Returns all games with 1 exclusion platform")
        @Order(18)
        void returnsAllGamesWithOneExclusionPlatform() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setExcludedPlatforms(List.of("Playstation 5"));
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(1, foundGames.size());
            Assertions.assertEquals("The Legend of Zelda: Breath of the Wild", foundGames.get(0).getName());
        }

        @Test
        @Transactional
        @Description("Returns all games with multiple exclusion platforms")
        @Order(19)
        void returnsAllGamesWithMultipleExclusionPlatforms() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setExcludedPlatforms(List.of("Playstation 5", "Xbox One"));
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(1, foundGames.size());
            Assertions.assertEquals("The Legend of Zelda: Breath of the Wild", foundGames.get(0).getName());
        }

        @Test
        @Transactional
        @Description("Returns all games with 1 exclusion tag")
        @Order(20)
        void returnsAllGamesWithOneExclusionTag() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setExcludedTags(List.of("Multiplayer"));
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(5, foundGames.size());
            Assertions.assertTrue(foundGames.stream().noneMatch(gameDTO -> gameDTO.getName().equals("Rocket League")));
            Assertions.assertTrue(foundGames.stream().noneMatch(gameDTO -> gameDTO.getName().equals("FIFA 11")));
        }

        @Test
        @Transactional
        @Description("Returns all games with multiple exclusion tags")
        @Order(21)
        void returnsAllGamesWithMultipleExclusionTags() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setExcludedTags(List.of("Singleplayer", "3D"));
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(0, foundGames.size());
        }

        @Test
        @Transactional
        @Description("Returns all games when provided invalid exclusion tag")
        @Order(22)
        void returnsAllGamesWithInvalidExclusionFilters() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setExcludedPlatforms(List.of("Not-A-Platform"));
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(7, foundGames.size());
        }

        @Test
        @Transactional
        @Description("Returns 0 games when no games match the provided exclusion filters")
        @Order(23)
        void returnsAllGamesWithTooManyExclusionFilters() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setExcludedGenres(List.of("Action", "Sports", "Turn-based strategy"));
            gameQueryFilters.setExcludedPlatforms(List.of("Playstation 5", "Xbox One"));
            gameQueryFilters.setExcludedTags(List.of("Multiplayer", "Singleplayer"));
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(0, foundGames.size());
        }

        @Test
        @Transactional
        @Description("Returns correct amount of games when provided 3 exclusion genres, 4 exclusion platforms and 2 exclusion tags")
        @Order(24)
        void returnsCorrectGamesWithManyExclusionFilters() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setExcludedGenres(List.of("Sports", "Turn-based strategy", "Stealth"));
            gameQueryFilters.setExcludedPlatforms(List.of("Playstation 5", "Xbox One", "Windows", "macOS"));
            gameQueryFilters.setExcludedTags(List.of("Multiplayer", "Sandbox"));
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(1, foundGames.size());
            Assertions.assertEquals("The Legend of Zelda: Breath of the Wild", foundGames.get(0).getName());
        }

        @Test
        @Transactional
        @Description("Returns correct amount of games when provided 1 exclusion genre, 1 exclusion platform and 1 exclusion tag")
        @Order(25)
        void returnsCorrectGamesWithManyExclusionFilters2() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setExcludedGenres(List.of("Action"));
            gameQueryFilters.setExcludedPlatforms(List.of("macOS"));
            gameQueryFilters.setExcludedTags(List.of("Singleplayer"));
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(1, foundGames.size());
            Assertions.assertEquals("FIFA 11", foundGames.get(0).getName());
        }

        @Test
        @Transactional
        @Description("Returns correct amount of games when provided 3 exclusion genres")
        @Order(26)
        void returnsCorrectGamesWith3ExclusionGenres() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setExcludedGenres(List.of("Sports", "Stealth", "Turn-based strategy"));
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(3, foundGames.size());
            Assertions.assertTrue(foundGames.stream().anyMatch(gameDTO -> gameDTO.getName().equals("Dark Souls: Remastered")));
            Assertions.assertTrue(foundGames.stream().anyMatch(gameDTO -> gameDTO.getName().equals("The Legend of Zelda: Breath of the Wild")));
            Assertions.assertTrue(foundGames.stream().anyMatch(gameDTO -> gameDTO.getName().equals("Dark Souls II Scholar of the First Sin")));
        }

        @Test
        @Transactional
        @Description("Returns correct amount of games when provided 3 exclusion platforms")
        @Order(27)
        void returnsCorrectGamesWith3ExclusionPlatforms() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setExcludedPlatforms(List.of("Playstation 5", "Xbox One", "Windows"));
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(1, foundGames.size());
            Assertions.assertEquals("The Legend of Zelda: Breath of the Wild", foundGames.get(0).getName());
        }

        @Test
        @Transactional
        @Description("Returns correct amount of games when provided 3 exclusion tags")
        @Order(28)
        void returnsCorrectGamesWith3ExclusionTags() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setExcludedTags(List.of("Sandbox", "Fantasy", "Singleplayer"));
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(2, foundGames.size());
            Assertions.assertTrue(foundGames.stream().anyMatch(gameDTO -> gameDTO.getName().equals("FIFA 11")));
            Assertions.assertTrue(foundGames.stream().anyMatch(gameDTO -> gameDTO.getName().equals("Rocket League")));
        }

        @Test
        @Description("Returns all games by name through searching")
        @Transactional
        @Order(29)
        void returnsAllGamesSortedBySearch() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setSearch("Persona 5");
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(1, foundGames.size());
            Assertions.assertEquals("Persona 5", foundGames.get(0).getName());
        }

        @Test
        @Description("Returns all games by name through searching and returning multiple games")
        @Transactional
        @Order(30)
        void returnsAllGamesSortedBySearches() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setSearch("souls");
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(2, foundGames.size());
        }

        @Test
        @Description("Returns all games with a specified year")
        @Transactional
        @Order(31)
        void returnsGamesWithSpecificYear() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setYear(2014);
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(2, foundGames.size());
        }

        @Test
        @Description("Returns one games with a specified year")
        @Transactional
        @Order(32)
        void returnsOneGameWithSpecificYear() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setYear(2021);
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(1, foundGames.size());
        }

        @Test
        @Description("Returns all games sorted by name")
        @Transactional
        @Order(32)
        void returnsAllGamesSortedByName() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setSortBy("name");
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(7, foundGames.size());
            Assertions.assertEquals("Assassin's Creed", foundGames.get(0).getName());
            Assertions.assertEquals("Dark Souls II Scholar of the First Sin", foundGames.get(1).getName());
            Assertions.assertEquals("Dark Souls: Remastered", foundGames.get(2).getName());
            Assertions.assertEquals("FIFA 11", foundGames.get(3).getName());
            Assertions.assertEquals("Persona 5", foundGames.get(4).getName());
            Assertions.assertEquals("Rocket League", foundGames.get(5).getName());
            Assertions.assertEquals("The Legend of Zelda: Breath of the Wild", foundGames.get(6).getName());
        }

        @Test
        @Description("Returns all games sorted by newest_releases")
        @Transactional
        @Order(33)
        void returnsAllGamesSortedByNewestRelease() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setSortBy("newest_releases");
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(7, foundGames.size());
            Assertions.assertEquals("Rocket League", foundGames.get(0).getName());
            Assertions.assertEquals("The Legend of Zelda: Breath of the Wild", foundGames.get(1).getName());
            Assertions.assertEquals("Dark Souls II Scholar of the First Sin", foundGames.get(2).getName());
            Assertions.assertEquals("Persona 5", foundGames.get(3).getName());
            Assertions.assertEquals("Dark Souls: Remastered", foundGames.get(4).getName());
            Assertions.assertEquals("FIFA 11", foundGames.get(5).getName());
            Assertions.assertEquals("Assassin's Creed", foundGames.get(6).getName());
        }

        @Test
        @Description("Returns all games sorted by oldest_releases")
        @Transactional
        @Order(34)
        void returnsAllGamesSortedByOldestRelease() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setSortBy("oldest_releases");
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(7, foundGames.size());
            Assertions.assertEquals("Assassin's Creed", foundGames.get(0).getName());
            Assertions.assertEquals("FIFA 11", foundGames.get(1).getName());
            Assertions.assertEquals("Dark Souls: Remastered", foundGames.get(2).getName());
            Assertions.assertEquals("Persona 5", foundGames.get(3).getName());
            Assertions.assertEquals("Dark Souls II Scholar of the First Sin", foundGames.get(4).getName());
            Assertions.assertEquals("The Legend of Zelda: Breath of the Wild", foundGames.get(5).getName());
            Assertions.assertEquals("Rocket League", foundGames.get(6).getName());
        }

        @Test
        @Description("Returns all games sorted by avg_score")
        @Transactional
        @Order(35)
        void returnsAllGamesSortedByAvgScore() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setSortBy("avg_score");
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(7, foundGames.size());
            Assertions.assertEquals("The Legend of Zelda: Breath of the Wild", foundGames.get(0).getName());
            Assertions.assertEquals("Dark Souls II Scholar of the First Sin", foundGames.get(1).getName());
            Assertions.assertEquals("Dark Souls: Remastered", foundGames.get(2).getName());
            Assertions.assertEquals("Rocket League", foundGames.get(3).getName());
            Assertions.assertEquals("FIFA 11", foundGames.get(4).getName());
            Assertions.assertEquals("Persona 5", foundGames.get(5).getName());
            Assertions.assertEquals("Assassin's Creed", foundGames.get(6).getName());
        }

        @Test
        @Description("Returns all games sorted by total_rating")
        @Transactional
        @Order(36)
        void returnsAllGamesSortedByTotalRating() {
            GameQueryFilters gameQueryFilters = new GameQueryFilters();
            gameQueryFilters.setSortBy("total_rating");
            gameQueryFilters.setLimit(10);

            List<GameDTO> foundGames = gameService.getAllGames(gameQueryFilters);
            Assertions.assertEquals(7, foundGames.size());
            Assertions.assertEquals("Assassin's Creed", foundGames.get(0).getName());
            Assertions.assertEquals("FIFA 11", foundGames.get(1).getName());
            Assertions.assertEquals("Dark Souls: Remastered", foundGames.get(2).getName());
            Assertions.assertEquals("Persona 5", foundGames.get(3).getName());
            Assertions.assertEquals("Dark Souls II Scholar of the First Sin", foundGames.get(4).getName());
            Assertions.assertEquals("Rocket League", foundGames.get(5).getName());
            Assertions.assertEquals("The Legend of Zelda: Breath of the Wild", foundGames.get(6).getName());
        }
    }
}
