package com.game.gamelist;

import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.User;
import com.game.gamelist.repository.PostRepository;
import com.game.gamelist.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class GamelistApplicationTests {

	@Container
	public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:13.11")
			.withUsername("changli")
			.withPassword("941210")
			.withDatabaseName("graphql_game_list_test");

	@DynamicPropertySource
	static void properties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", container::getJdbcUrl);
		registry.add("spring.datasource.username", container::getUsername);
		registry.add("spring.datasource.password", container::getPassword);
	}

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	public GamelistApplicationTests() {
		log.info("In constructor class instance: " + this);
	}

	@Test
	void contextLoads() {
		System.out.println("Hello World 👹👹👹👹👹");
		assertThat(1).isEqualTo(1);

		User user = new User();
		user.setUsername("changli");
		user.setEmail("changli@gmail.com");
		user.setPassword("123456");
		userRepository.save(user);

		System.out.println("user ID  👹👹👹👹👹: " + user.getId());

		Post post = new Post();
		post.setText("Hello World");
		post.setUser(user);
		postRepository.save(post);

		System.out.println("post ID  👹👹👹👹👹: " + post.getId());
	}
}