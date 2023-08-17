package com.game.gamelist;

import com.game.gamelist.entity.GameJournal;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
@JsonTest
public class GameListJsonTests {
    @Autowired
    private JacksonTester<GameJournal> json;

    @Autowired
    private JacksonTester<GameJournal[]> jsonList;

    private GameJournal[] gameJournals;

    @BeforeEach
    void setUp() {
        GameJournal gameJournal1 = GameJournal.builder()
                .content("Test Content 99")
                .build();
        GameJournal gameJournal2 = GameJournal.builder()
                .content("Test Content 100")
                .build();
        GameJournal gameJournal3 = GameJournal.builder()
                .content("Test Content 101")
                .build();

        gameJournals = Arrays.array(gameJournal1, gameJournal2, gameJournal3);

    }

    @Test
    public void gameJournalSerializationTest() throws IOException, IllegalAccessException {
        GameJournal gameJournal = gameJournals[0];

        assertThat(json.write(gameJournal)).isStrictlyEqualToJson("gameJournal.json");
        assertThat(json.write(gameJournal)).hasJsonPathStringValue("@.content");
        assertThat(json.write(gameJournal)).extractingJsonPathStringValue("@.content")
                .isEqualTo("Test Content 99");
    }

    @Test
    void gameJournalsListSerializationTest() throws IOException {
        assertThat(jsonList.write(gameJournals)).isStrictlyEqualToJson("gameJournalslist.json");
    }

}


