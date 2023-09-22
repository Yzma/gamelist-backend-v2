package com.game.gamelist.repository;

import com.game.gamelist.config.ContainersEnvironment;
import com.game.gamelist.dto.PostDTO;
import com.game.gamelist.entity.*;
import com.game.gamelist.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private GameRepository gameRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserGameRepository userGameRepository;

    @Autowired
    private StatusUpdateRepository statusUpdateRepository;

    @Autowired
    private LikeRepository likeRepository;

    private final PostMapper postMapper;

    @Autowired
    public InteractiveEntityRepositoryTests(PostMapper postMapper) {
        this.postMapper = postMapper;
    }

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

            Game game = Game.builder().name("Game1").build();
            gameRepository.save(game);

            UserGame userGame = UserGame.builder().user(user).game(game).gameStatus(GameStatus.Playing).build();

            StatusUpdate statusUpdate = StatusUpdate.builder().gameStatus(GameStatus.Playing).userGame(userGame).likes(new ArrayList<>()).build();

            LikeEntity likeEntity = LikeEntity.builder().interactiveEntity(statusUpdate).user(user).build();
            System.out.println("👹");

            statusUpdate.addLike(likeEntity);

            statusUpdateRepository.save(statusUpdate);
            userGameRepository.save(userGame);

            likeRepository.save(likeEntity);


            User user2 = User.builder().username("changli2").email("changli2@gmail.com").password("123456").userPicture("userpic").bannerPicture("userBanner").updatedAt(LocalDateTime.now()).build();
            userRepository.save(user2);

            Post post1 = new Post();
            post1.setText("Hello World");
            post1.setUser(user);
            post1.setLikes(new ArrayList<>());
            post1.setCreatedAt(LocalDateTime.now());
            post1.setUpdatedAt(LocalDateTime.now());

            LikeEntity likeOnPost1 = LikeEntity.builder().interactiveEntity(post1).user(user).build();
            post1.addLike(likeOnPost1);
            System.out.println("👹👹");
            postRepository.save(post1);
            likeRepository.save(likeOnPost1);

            Post post2 = new Post();
            post2.setText("Another Post");
            post2.setUser(user);
            post2.setLikes(new ArrayList<>());
            post2.setCreatedAt(LocalDateTime.now());
            post2.setUpdatedAt(LocalDateTime.now());

            LikeEntity likeOnPost2 = LikeEntity.builder().interactiveEntity(post2).user(user).build();
            post2.addLike(likeOnPost2);
            System.out.println("👹👹👹");
            postRepository.save(post2);
            likeRepository.save(likeOnPost2);

            Post post3 = Post.builder().text("Post from changli2").user(user2).createdAt(LocalDateTime.now()).likes(new ArrayList<>()).updatedAt(LocalDateTime.now()).build();
            LikeEntity likeOnPost3 = LikeEntity.builder().interactiveEntity(post3).user(user2).build();
            post3.addLike(likeOnPost3);
            System.out.println("👹👹👹👹");
            postRepository.save(post3);
            likeRepository.save(likeOnPost3);
        }

        @Test
        @Order(1)
        @Transactional
        public void whenFindAllPostsAndStatusUpdates_Expect_ListWithThree() {

            User user = userRepository.findByEmail("changli@gmail.com").get();

            List<InteractiveEntity> interactiveEntityList = interactiveEntityRepository.findAllPostsAndStatusUpdatesByUserId(user.getId());

            for (InteractiveEntity interactiveEntity : interactiveEntityList) {
                if (interactiveEntity instanceof Post) {
                    System.out.println("Post: " + ((Post) interactiveEntity).getText());

                    PostDTO postDTO = postMapper.postToPostDTO((Post) interactiveEntity);

                    System.out.println("PostDTO: " + postDTO.getText());
                    System.out.println("Likes: " + postDTO.getLikes());

                    System.out.println("Likes: " + postDTO.getLikes().get(0).getUser().getUsername());
                    System.out.println("Comments: " + postDTO.getComments());
                    System.out.println("User: " + postDTO.getUser());

                } else if (interactiveEntity instanceof StatusUpdate) {
                    System.out.println("StatusUpdate: " + ((StatusUpdate)interactiveEntity).getGameStatus());
                    System.out.println("Likes: " + (interactiveEntity).getLikes());
                }

            }

            assertEquals(3, interactiveEntityRepository.findAllPostsAndStatusUpdatesByUserId(user.getId()).size());
        }
    }
}
