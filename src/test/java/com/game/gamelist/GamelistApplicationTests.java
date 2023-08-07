package com.game.gamelist;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class GamelistApplicationTests {

	public static PostgreSQLContainer container = new PostgreSQLContainer().withUsername("changli").withPassword("941210").withDatabaseName("graphql_game_list_test");
	@Test

	void contextLoads() {
		System.out.println("Hello World 👹👹👹👹👹");
		assertThat(1).isEqualTo(1);
	}

}
