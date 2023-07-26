package com.game.gamelist;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class GamelistApplicationTests {


	@Test
	void contextLoads() {
		assertThat(1).isEqualTo(42);
	}

}
