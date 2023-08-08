package com.game.gamelist;

import com.game.gamelist.containers.PostgresTestContainer;
import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.User;
import com.game.gamelist.repository.PostRepository;
import com.game.gamelist.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class GamelistApplicationTests {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	static PostgreSQLContainer<?> container = new PostgreSQLContainer<>(
			"postgres:15-alpine"
	);


	public GamelistApplicationTests() {
		System.out.println("In constructor class instance: " + this);
	}

	@BeforeEach
	public void beforeEachMethod() {
		User user = new User();
		user.setUsername("changli");
		user.setEmail("changli@gmail.com");
		user.setPassword("123456");
		user.setUpdatedAt(LocalDateTime.now());
		userRepository.save(user);

		System.out.println("user ID  👹👹👹👹👹: " + user.getId());

		Post post1 = new Post();
		post1.setText("Hello World");
		post1.setUser(user);
		post1.setId(1L);
		post1.setCreatedAt(LocalDateTime.now());
		post1.setUpdatedAt(LocalDateTime.now());
		postRepository.save(post1);

		Post post2 = new Post();
		post2.setText("Another Post");
		post2.setUser(user);
		post2.setCreatedAt(LocalDateTime.now());
		post2.setUpdatedAt(LocalDateTime.now());
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