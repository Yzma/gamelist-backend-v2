package com.game.gamelist.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.game.gamelist.config.SecurityTestConfig;
import com.game.gamelist.entity.*;
import com.game.gamelist.service.UserGameService;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@Import(SecurityTestConfig.class)
public class UserGameControllerTests {

    @InjectMocks
    private UserGameController userGameController;
    @MockBean
    private UserGameService userGameService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private SecurityTestConfig.Auth0JwtTestUtils auth0JwtTestUtils;

    @Test
    void contextLoads() {
        Assertions.assertNotNull(userGameController);
    }

    private Game game1;
    private Game game2;
    private Game game3;
    private User principal;
    private User notPrincipal;
    private Game game4;
    @BeforeEach
    void beforeEachTest() {
        game1 = Game.builder().name("game1").description("description of game1").build();
        game2 = Game.builder().name("game2").description("description of game2").build();
        game3 = Game.builder().name("game3").description("description of game3").build();
        game4 = Game.builder().name("game4").description("description of game4").build();

        principal = User.builder().id(1L).roles(Set.of(Role.ROLE_ADMIN)).username("principal").email("principal@gmail.com").build();

        notPrincipal = User.builder().id(2L).roles(Set.of(Role.ROLE_USER)).username("notPrincipal").email("notprincipal@gmail.com").build();

    }

    @Test
    void when_send_get_request_to_userGame_endpoint_return_allUserGameByUserId() throws Exception {
        auth0JwtTestUtils.mockAuthentication(principal);

        UserGame userGame1 = UserGame.builder().id(1L).user(principal).game(game1).gameNote("GameNote from usergame of user1 and game1").gameStatus(GameStatus.Completed).rating(9).build();

        UserGame userGame2 = UserGame.builder().id(2L).user(principal).game(game2).gameNote("GameNote from usergame of user1 and game2").gameStatus(GameStatus.Planning).rating(2).build();

        UserGame userGame3 = UserGame.builder().id(3L).user(principal).game(game3).gameNote("GameNote from usergame of user1 and game3").gameStatus(GameStatus.Playing).rating(5).build();

        UserGame userGame4 = UserGame.builder().id(4L).user(principal).game(game4).gameNote("GameNote from usergame of user1 and game4").gameStatus(GameStatus.Paused).rating(7).build();


        Set<UserGame> userGameSet = Set.of(userGame1, userGame2, userGame3, userGame4);

        when(userGameService.findAllUserGamesByUserId(principal)).thenReturn(userGameSet);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/usergames/")
                        .contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(userGameSet)))
                .andExpect(status().isOk())
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString().contains("game1")))
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString().contains("game2")))
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString().contains("game3")))
                .andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString().contains("game4")));
    }

    @Test
    void when_send_get_request_to_userGame_endpoint_with_id_should_return_userGame() throws Exception {
        auth0JwtTestUtils.mockAuthentication(principal);

        UserGame userGame1 = UserGame.builder().id(1L).user(principal).game(game1).gameNote("GameNote from usergame of user1 and game1").gameStatus(GameStatus.Completed).rating(9).build();

        when(userGameService.findUserGameById(1L, principal)).thenReturn(userGame1);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/usergames/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(result -> Assertions.assertTrue(result.getResponse().getContentAsString().contains("game1"))).andExpect(jsonPath("$.data.userGame.gameNote").value("GameNote from usergame of user1 and game1"));
    }


}
