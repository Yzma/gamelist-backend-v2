package com.game.gamelist;

import com.game.gamelist.config.ContainersEnvironment;
import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.User;
import com.game.gamelist.repository.PostRepository;
import com.game.gamelist.repository.UserRepository;
import jakarta.transaction.Transactional;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;



import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = GamelistApplication.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
class GamelistApplicationTests extends ContainersEnvironment {

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	public void beforeEachMethod() {
		User user = new User();
		user.setUsername("changli");
		user.setEmail("changli@gmail.com");
		user.setPassword("123456");
		user.setUpdatedAt(LocalDateTime.now());
		userRepository.save(user);

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

	}

	@Test
	@Order(1)
	@Transactional
	void contextLoads() {
		assertThat(1).isEqualTo(1);
		List<Post> postList = postRepository.findAll();
		assertThat(postList.size()).isEqualTo(2);
	}

	@Test
	@Order(2)
	@Transactional
	void testPost() {
		List<Post> postList = postRepository.findAll();

		assertThat(postList.size()).isEqualTo(2);

		Post post = postList.get(0);
		assertThat(post.getText()).isEqualTo("Hello World");
		Post post2 = postList.get(1);
		assertThat(post2.getText()).isEqualTo("Another Post");
	}

}