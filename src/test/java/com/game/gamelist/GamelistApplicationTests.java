package com.game.gamelist;

import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.User;
import com.game.gamelist.repository.PostRepository;
import com.game.gamelist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
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
		System.out.println("In constructor class instance: " + this);
	}

	@BeforeEach
	public void beforeEachMethod() {
		User user = new User();
		user.setUsername("changli");
		user.setEmail("changli@gmail.com");
		user.setPassword("123456");
		userRepository.save(user);

		System.out.println("user ID  👹👹👹👹👹: " + user.getId());

		Post post1 = new Post();
		post1.setText("Hello World");
		post1.setUser(user);
		post1.setId(1L);
		postRepository.save(post1);

		Post post2 = new Post();
		post2.setText("Another Post");
		post2.setUser(user);
		post2.setId(2L);
		postRepository.save(post2);


		System.out.println("post ID  👹👹👹👹👹: " + post1.getId());
		System.out.println("post ID  👹👹👹👹👹: " + post2.getId());
		System.out.println("In beforeEachMethod class instance: " + this);
		System.out.println("In beforeEachMethod Container ID: " + container.getContainerId());
	}

	@Test
	void contextLoads() {
		System.out.println("Hello World 👹👹👹👹👹");
		assertThat(1).isEqualTo(1);
	}

	@Test
	void testPost() {
		System.out.println("In testPost class instance: " + this);
		System.out.println("In testPost Container ID: " + container.getContainerId());
		Post post = postRepository.findById(1L).orElseThrow();
		assertThat(post.getText()).isEqualTo("Hello World");
		Post post2 = postRepository.findById(2L).orElseThrow();
		assertThat(post2.getText()).isEqualTo("Another Post");
	}
}