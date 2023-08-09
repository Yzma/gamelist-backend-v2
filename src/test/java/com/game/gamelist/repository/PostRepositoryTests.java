package com.game.gamelist.repository;

import com.game.gamelist.config.ContainersEnvironment;
import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.User;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
public class PostRepositoryTests extends ContainersEnvironment {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;


    @Test
    @Transactional
    public void whenFindAll_Expect_EmptyList() {

        List<Post> postList = postRepository.findAll();

        assertEquals(0, postList.size());

    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class PostRepositoryCRUDTests {

        @BeforeEach
        void beforeEachTest() {
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
        }

        @Test
        @Order(1)
        @Transactional
        public void whenFindAll_Expect_ListWithTwo() {
            List<Post> postListInit = postRepository.findAll();
            assertEquals(2, postListInit.size());

        }

        @Test
        @Transactional
        @Order(2)
        public void whenFindById_Expect_Post() {

            User user = userRepository.findByEmail("changli@gmail.com").orElse(null);

            assertEquals("changli", user.getUsername());

            List<Post> allPosts = postRepository.findAll();
            assertEquals("Hello World", allPosts.get(0).getText());
            assertEquals("Another Post", allPosts.get(1).getText());

            assertEquals(2, postRepository.findAll().size());

            Optional<Post> optionalPost = postRepository.findById(3L);
            Post post = optionalPost.get();

            assertEquals("Hello World", post.getText());
            assertEquals(3L, post.getId());

            Post post2 = postRepository.findById(4L).get();

            assertNotEquals(post2.getText(), post.getText());
            assertNotEquals(4L, post.getId());
            assertEquals(post2.getUser(), post.getUser());
        }

        @Test
        @Transactional
        @Order(3)
        void whenFindAllByUserId_Expect_ListWithTwo() {
            User user = userRepository.findByEmail("changli@gmail.com").orElse(null);
            assertEquals("changli", user.getUsername());

            List<Post> postList = new ArrayList<>(postRepository.findAllByUserId(user.getId()).get());

            assertEquals(2, postList.size());

            Post firstPost = postList.get(0);
            Post secondPost = postList.get(1);
            assertEquals("Hello World", firstPost.getText());
            assertEquals("Another Post", secondPost.getText());

        }

        @Test
        @Transactional
        @Order(4)
        void whenUpdatePost_Expect_PostUpdated() {
            User user = userRepository.findByEmail("changli@gmail.com").orElse(null);
            assertEquals("changli", user.getUsername());

            Post post = postRepository.findById(7L).get();

            assertEquals("Hello World", post.getText());
            post.setText("Hello World Updated");
            postRepository.save(post);

            Post postUpdated = postRepository.findById(7L).get();
            assertEquals("Hello World Updated", postUpdated.getText());
            assertEquals(post.getId(), postUpdated.getId());
            assertEquals(post.getUser(), postUpdated.getUser());
        }

        @Test
        @Transactional
        @Order(5)
        void whenDeletePost_Expect_PostDeleted() {
            User user = userRepository.findByEmail("changli@gmail.com").orElse(null);
            assertEquals("changli", user.getUsername());

            List<Post> postList = postRepository.findAll();
            assertEquals(2, postList.size());
            Post firstPost = postList.get(0);

            postRepository.delete(firstPost);

            List<Post> postListAfterDelete = postRepository.findAll();
            assertEquals(1, postListAfterDelete.size());

            Post post = postListAfterDelete.get(0);
            assertNotEquals(firstPost.getId(), post.getId());
            assertNotEquals(firstPost.getText(), post.getText());
            assertEquals(firstPost.getUser(), post.getUser());
        }
    }
}
