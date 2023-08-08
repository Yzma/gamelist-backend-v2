package com.game.gamelist.api.repository;

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
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.PostgreSQLContainer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class PostRepositoryTests {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>( "postgres:15-alpine").withUsername("changli").withPassword("123456").withDatabaseName("test_db");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }
//    @AfterEach
//    public void afterEachMethod() {
//        postRepository.deleteAll();
//        userRepository.deleteAll();
//    }
    @BeforeAll
    public static void beforeEachMethod() {
        container.start();
    }

    @AfterAll
    public static void afterEachMethod() {
        container.stop();
    }



    @Test
    @Transactional
    public void whenFindAll_Expect_EmptyList() {

        System.out.println("In test method instance: " + this);
        System.out.println("In test method instance: " + postRepository.findAll());
        List<Post> postList = postRepository.findAll();

        assertEquals(0, postList.size());

    }

    @Test
    @Transactional
    public void whenFindAll_Expect_ListWithTwo() {
        List<Post> postListInit = postRepository.findAll();
        assertEquals(0, postListInit.size());

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
    @Transactional
    public void whenFindById_Expect_Post() {

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
    post1.setId(5L);
    post1.setCreatedAt(LocalDateTime.now());
    post1.setUpdatedAt(LocalDateTime.now());
    postRepository.save(post1);

    Post post2 = new Post();
    post2.setText("Another Post");
    post2.setUser(user);
    post2.setCreatedAt(LocalDateTime.now());
    post2.setUpdatedAt(LocalDateTime.now());
    post2.setId(6L);
    postRepository.save(post2);

    List<Post> allPosts = postRepository.findAll();
    assertEquals("Hello World", allPosts.get(0).getText());

        for (Post post : allPosts) {
            System.out.println(" 👹👹👹👹👹 👹👹👹👹👹Post: " + post.getText());
            System.out.println(" ������ Post Id: " + post.getId());
        }

    System.out.println("First Post: " + postRepository.findById(5L).get().getText());

    assertEquals(2, postRepository.findAll().size());

    Optional<Post> optionalPost = postRepository.findById(5L);
    Post post = optionalPost.get();

    assertEquals("Hello World", post.getText());
    assertEquals(5L, post.getId());

    assertNotEquals(post2.getText(), post.getText());
    assertNotEquals(6L, post.getId());
    assertEquals(post2.getUser(), post.getUser());
    }

    @Test
    @Transactional
    void whenFindAllByUserId_Expect_ListWithTwo() {

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
        post1.setId(3L);
        post1.setCreatedAt(LocalDateTime.now());
        post1.setUpdatedAt(LocalDateTime.now());
        postRepository.save(post1);

        Post post2 = new Post();
        post2.setText("Another Post");
        post2.setUser(user);
        post2.setCreatedAt(LocalDateTime.now());
        post2.setUpdatedAt(LocalDateTime.now());
        post2.setId(4L);
        postRepository.save(post2);

        List<Post> postList = new ArrayList<>( postRepository.findAllByUserId(user.getId()).get());

        assertEquals(2, postList.size());

        Post firstPost = postList.get(0);
        Post secondPost = postList.get(1);
        assertEquals("Hello World", firstPost.getText());
        assertEquals("Another Post", secondPost.getText());

    }

}
