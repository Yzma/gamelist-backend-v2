package com.game.gamelist.repository;


import com.game.gamelist.config.ContainersEnvironment;
import com.game.gamelist.entity.LikeEntity;
import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.User;
import com.game.gamelist.exception.ResourceNotFoundException;
import com.game.gamelist.model.LikeEntityView;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @PersistenceContext
    private EntityManager entityManager;

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
            user.setUserPicture("User Picture URL");
            user.setBannerPicture("Banner Picture URL");
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
            User user = userRepository.findByEmail("changli@gmail.com").orElseThrow(() -> new ResourceNotFoundException("Can not find user with email:"));

            Post post = postRepository.findAll().get(0);


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

            LikeEntityView likeEntityView = likeRepository.findLikeEntityViewById(1L).orElseThrow(() -> new ResourceNotFoundException("Can not find like entity with id: " + 1L));

            assertEquals("changli", likeEntityView.getUser().getUsername());
            assertEquals("User Picture URL", likeEntityView.getUser().getUserPicture());

            assertEquals("changli", likeEntity1.getUser().getUsername());
            assertEquals("Hello World", ((Post) likeEntity1.getInteractiveEntity()).getText());
            assertEquals(likeEntity1.getId(), likeEntity.getId());
            assertEquals(likeEntity1.getUser(), likeEntity.getUser());
            assertEquals(likeEntity1.getInteractiveEntity(), likeEntity.getInteractiveEntity());
            entityManager.refresh(post);

            LikeEntity likeEntityFromPost = post.getLikes().get(0);
            assertEquals(1, post.getLikes().size());

            assertEquals(likeEntityFromPost.getId(), likeEntity.getId());
            assertEquals(likeEntityFromPost.getUser(), likeEntity.getUser());
        }

        @Test
        @Order(2)
        @Transactional
        public void when_existsByUserAndInteractiveEntityId_Expect_True() {
            User user = userRepository.findByEmail("changli@gmail.com").orElseThrow(() -> new ResourceNotFoundException("Can not find user with email:"));

            Post post = postRepository.findAll().get(0);

            LikeEntity likeEntity = new LikeEntity();
            likeEntity.setUser(user);
            likeEntity.setInteractiveEntity(post);
            likeEntity.setCreatedAt(LocalDateTime.now());
            likeEntity.setUpdatedAt(LocalDateTime.now());

            likeRepository.save(likeEntity);

            assertEquals(1, likeRepository.findAll().size());
            boolean isExists = likeRepository.existsByUserIdAndInteractiveEntityId(user.getId(), post.getId());

            assertEquals(true, isExists);

        }

        @Test
        @Order(3)
        @Transactional
        public void when_findByUserIdAndInteractiveEntityId_Expect_LikeEntity() {
            User user = userRepository.findByEmail("changli@gmail.com").orElseThrow(() -> new ResourceNotFoundException("Can not find user with email:"));

            Post post = postRepository.findAll().get(0);

            LikeEntity likeEntity = new LikeEntity();
            likeEntity.setUser(user);
            likeEntity.setInteractiveEntity(post);
            likeEntity.setCreatedAt(LocalDateTime.now());
            likeEntity.setUpdatedAt(LocalDateTime.now());

            likeRepository.save(likeEntity);

            assertEquals(1, likeRepository.findAll().size());


            LikeEntity likeEntityFromDB = likeRepository.findByUserIdAndInteractiveEntityId(user.getId(), post.getId()).get();

            entityManager.refresh(post);
            entityManager.refresh(likeEntity);

            assertEquals(likeEntityFromDB.getId(), likeEntity.getId());
            assertEquals(likeEntityFromDB.getUser(), likeEntity.getUser());
            assertEquals(likeEntityFromDB.getInteractiveEntity(), likeEntity.getInteractiveEntity());
        }

        @Test
        @Order(4)
        @Transactional
        public void when_deleteByUserIdAndInteractiveEntityId_Expect_EmptyList() {
            User user = userRepository.findByEmail("changli@gmail.com").orElseThrow(() -> new ResourceNotFoundException("Can not find user with email:"));

            Post post = postRepository.findAll().get(0);

            LikeEntity likeEntity = new LikeEntity();
            likeEntity.setUser(user);
            likeEntity.setInteractiveEntity(post);

            likeRepository.save(likeEntity);
            entityManager.refresh(post);

            post.getLikes().add(likeEntity);

            assertEquals(1, post.getLikes().size());

            assertEquals(1, likeRepository.findAll().size());

            likeRepository.deleteByUserIdAndInteractiveEntityId(user.getId(), post.getId());

            post.getLikes().remove(likeEntity);

            assertEquals(0, post.getLikes().size());

            assertEquals(0, likeRepository.findAll().size());

            entityManager.flush();

            LikeEntity likeEntityFromDB = likeRepository.findByUserIdAndInteractiveEntityId(user.getId(), post.getId()).orElse(null);

            assertEquals(null, likeEntityFromDB);
        }

    }
}
