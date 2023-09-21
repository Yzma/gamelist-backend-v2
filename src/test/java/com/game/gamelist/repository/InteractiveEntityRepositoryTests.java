package com.game.gamelist.repository;

import com.game.gamelist.config.ContainersEnvironment;
import com.game.gamelist.entity.InteractiveEntity;
import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.User;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
public class InteractiveEntityRepositoryTests extends ContainersEnvironment {

    @Autowired
    private InteractiveEntityRepository interactiveEntityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private StatusUpdateRepository statusUpdateRepository;

    @Test
    @Transactional
    public void whenFindAll_Expect_EmptyList() {
        assertEquals(0, interactiveEntityRepository.findAll().size());
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class InteractiveEntityRepositoryCRUDTests {

        @BeforeEach
        void beforeEachTest() {
            User user = new User();
            user.setUsername("changli");
            user.setEmail("changli@gmail.com");
            user.setPassword("123456");
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);

            User user2 = User.builder().username("changli2").email("changli2@gmail.com").password("123456").updatedAt(LocalDateTime.now()).build();
            userRepository.save(user2);

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

            Post post3 = Post.builder().text("Post from changli2").user(user2).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
            postRepository.save(post3);
        }

        @Test
        @Order(1)
        @Transactional
        public void whenFindAllPostsAndStatusUpdates_Expect_ListWithThree() {

            User user = userRepository.findByEmail("changli@gmail.com").get();

            List<InteractiveEntity> interactiveEntityList = interactiveEntityRepository.findAllPostsAndStatusUpdatesByUserId(user.getId());

            System.out.println(interactiveEntityList);

            assertEquals(2, interactiveEntityRepository.findAllPostsAndStatusUpdatesByUserId(user.getId()).size());
        }
    }
}
