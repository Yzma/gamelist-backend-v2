package com.game.gamelist.api.repository;



import com.game.gamelist.GamelistApplication;
import com.game.gamelist.config.ContainersEnvironment;
import com.game.gamelist.containers.PostgresTestContainer;
import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.User;
import com.game.gamelist.repository.PostRepository;
import com.game.gamelist.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = GamelistApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Rollback(value = false)
public class PostRepositoryTests extends ContainersEnvironment {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void afterEachMethod() {
        postRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void whenFindAll_Expect_EmptyList() {
        PostgresTestContainer container = new PostgresTestContainer();
        container.start();

        System.out.println("In test method instance: " + this);
        System.out.println("In test method instance: " + postRepository.findAll());
        List<Post> postList = postRepository.findAll();

        assertEquals(0, postList.size());
    }

    @Test
    public void whenFindAll_Expect_ListWithTwo() {

        PostgresTestContainer container = new PostgresTestContainer();
        container.start();

        User user = new User();
        user.setUsername("changli");
        user.setEmail("changli@gmail.com");
        user.setPassword("123456");
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        System.out.println("In test method instance: " + this);
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

        List<Post> postList = postRepository.findAll();

        assertEquals(2, postList.size());

    }

@Test
    public void whenFindById_Expect_Post() {
    PostgresTestContainer container = new PostgresTestContainer();
    container.start();

    User user = new User();
    user.setUsername("changli");
    user.setEmail("changli@gmail.com");
    user.setPassword("123456");
    user.setUpdatedAt(LocalDateTime.now());
    userRepository.save(user);

    System.out.println("In test method instance: " + this);
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

    List<Post> allPosts = postRepository.findAll();
    assertEquals("Hello World", allPosts.get(0).getText());

    System.out.println("First Post: " + postRepository.findById(1L).get().getText());

    assertEquals(2, postRepository.findAll().size());

    Optional<Post> optionalPost = postRepository.findById(1L);
    Post post = optionalPost.get();

    assertEquals("Hello World", post.getText());
    assertEquals(1L, post.getId());
    assertEquals(user, post.getUser());
    assertEquals(post1.getCreatedAt(), post.getCreatedAt());
    assertEquals(post1.getUpdatedAt(), post.getUpdatedAt());
    assertNotEquals(post2.getText(), post.getText());
    assertNotEquals(2L, post.getId());
    assertNotEquals(post2.getUser(), post.getUser());}

    @Test
    void whenFindAllByUserId_Expect_ListWithTwo() {
        PostgresTestContainer container = new PostgresTestContainer();
        container.start();

        User user = new User();
        user.setUsername("changli");
        user.setEmail("changli@gmail.com");
        user.setPassword("123456");
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        System.out.println("In test method instance: " + this);
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

        Set<Post> postList = postRepository.findAllByUserId(user.getId()).get();

        assertEquals(2, postList.size());
        assertEquals("Hello World", postList.iterator().next().getText());
        assertEquals("Another Post", postList.iterator().next().getText());

    }

}
