package com.game.gamelist.repository;


import com.game.gamelist.config.ContainersEnvironment;
import com.game.gamelist.entity.LikeEntity;
import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.User;
import com.game.gamelist.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
public class LikeRepositoryTests extends ContainersEnvironment {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private InteractiveEntityRepository interactiveEntityRepository;

    @Test
    @Transactional
    public void whenFindAll_Expect_EmptyList() {

         List<LikeEntity> likeEntityList = likeRepository.findAll();

         assertEquals(0, likeEntityList.size());

    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class LikeEntityRepositoryCRUDTests {

        @BeforeEach
        void beforeEachTest() {
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
        public void whenFindById_Expect_LikeEntity() {
            User user = userRepository.findByEmail("changli@gmail.com").orElseThrow(() -> new ResourceNotFoundException("Can not find user with email:" ));

            Post post = postRepository.findById(1L).get();

            assertEquals(0, post.getLikes().size());

            assertEquals("Hello World", post.getText());

            LikeEntity likeEntity = new LikeEntity();
            likeEntity.setUser(user);
            likeEntity.setInteractiveEntity(post);
            likeEntity.setCreatedAt(LocalDateTime.now());
            likeEntity.setUpdatedAt(LocalDateTime.now());
            likeEntity.setId(1L);

            likeRepository.save(likeEntity);

            assertEquals(1, likeRepository.findAll().size());

            LikeEntity likeEntity1 = likeRepository.findById(1L).get();

            assertEquals("changli", likeEntity1.getUser().getUsername());
            assertEquals("Hello World", ((Post)likeEntity1.getInteractiveEntity()).getText());
            assertEquals(likeEntity1.getId(), likeEntity.getId());
            assertEquals(likeEntity1.getUser(), likeEntity.getUser());
            assertEquals(likeEntity1.getInteractiveEntity(), likeEntity.getInteractiveEntity());
            Post postRefreshed = postRepository.findById(1L).get();
            assertEquals(1, postRefreshed.getLikes().size());
        }

    }
}
