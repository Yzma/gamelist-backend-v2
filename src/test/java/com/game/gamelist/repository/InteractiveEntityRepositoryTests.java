package com.game.gamelist.repository;

import com.game.gamelist.config.ContainersEnvironment;
import com.game.gamelist.dto.PostDTO;
import com.game.gamelist.dto.StatusUpdateDTO;
import com.game.gamelist.entity.*;
import com.game.gamelist.mapper.PostMapper;
import com.game.gamelist.mapper.StatusUpdateMapper;
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
    private GameJournalRepository gameJournalRepository;

    @Autowired
    private UserGameRepository userGameRepository;

    @Autowired
    private StatusUpdateRepository statusUpdateRepository;

    @Autowired
    private LikeRepository likeRepository;

    private final PostMapper postMapper;
    private final StatusUpdateMapper statusUpdateMapper;
    @Autowired
    public InteractiveEntityRepositoryTests(PostMapper postMapper, StatusUpdateMapper statusUpdateMapper) {
        this.postMapper = postMapper;
        this.statusUpdateMapper = statusUpdateMapper;
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

            GameJournal gameJournal = GameJournal.builder().user(user).content("about the game 1").createdAt(LocalDateTime.now()).build();
            Comment gameJournalComment = Comment.builder().user(user).text("comment on game journal").interactiveEntity(gameJournal).createdAt(LocalDateTime.now()).build();
            gameJournalRepository.save(gameJournal);
            commentRepository.save(gameJournalComment);


            UserGame userGame = UserGame.builder().user(user).game(game).gameStatus(GameStatus.Playing).build();

            StatusUpdate statusUpdate = StatusUpdate.builder().gameStatus(GameStatus.Playing).userGame(userGame).likes(new ArrayList<>()).build();

            LikeEntity likeEntity = LikeEntity.builder().interactiveEntity(statusUpdate).user(user).build();

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
            postRepository.save(post2);
            likeRepository.save(likeOnPost2);

            Post post3 = Post.builder().text("Post from changli2").user(user2).createdAt(LocalDateTime.now()).likes(new ArrayList<>()).updatedAt(LocalDateTime.now()).build();
            LikeEntity likeOnPost3 = LikeEntity.builder().interactiveEntity(post3).user(user2).build();
            post3.addLike(likeOnPost3);
            postRepository.save(post3);
            likeRepository.save(likeOnPost3);
        }

        @Test
        @Order(1)
        @Transactional
        public void whenFindAllPostsAndStatusUpdates_Expect_ListWithThree() {

            User user = userRepository.findByEmail("changli@gmail.com").get();

            List<InteractiveEntity> interactiveEntityList = interactiveEntityRepository.findAllPostsAndStatusUpdatesByUserId(user.getId());

            List<PostDTO> postDTOList = new ArrayList<>();
            List<StatusUpdateDTO> statusUpdateDTOList = new ArrayList<>();

            for (InteractiveEntity interactiveEntity : interactiveEntityList) {
                if (interactiveEntity instanceof Post) {
                    postDTOList.add(postMapper.postToPostDTO((Post) interactiveEntity));
                } else if (interactiveEntity instanceof StatusUpdate) {
                    statusUpdateDTOList.add(statusUpdateMapper.statusUpdateToStatusUpdateDTO((StatusUpdate) interactiveEntity));
                }

            }

            assertEquals(GameStatus.Playing, statusUpdateDTOList.get(0).getGameStatus());

            assertEquals(3, interactiveEntityRepository.findAllPostsAndStatusUpdatesByUserId(user.getId()).size());
        }

        @Test
        @Order(2)
        @Transactional
        public void when_findPostsAndStatusUpdatesByUserIdAndStartingWithIdDesc_Expect_objectWithLimitAndStartWithId() {
            User user = userRepository.findByEmail("changli@gmail.com").get();

            Post post1 = Post.builder().user(user).text("Post1").likes(new ArrayList<>()).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
            Post post2 = Post.builder().user(user).text("Post2").likes(new ArrayList<>()).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
            Post post3 = Post.builder().user(user).text("Post3").likes(new ArrayList<>()).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
            postRepository.save(post1);
            postRepository.save(post2);
            postRepository.save(post3);

            List<InteractiveEntity> interactiveEntityList = interactiveEntityRepository.findAllPostsAndStatusUpdatesByUserId(user.getId());

            InteractiveEntity interactiveEntity1 = interactiveEntityList.get(0);
            InteractiveEntity interactiveEntity2 = interactiveEntityList.get(1);
            InteractiveEntity interactiveEntity3 = interactiveEntityList.get(2);

            System.out.println("interactiveEntity1.getId() = " + interactiveEntity1.getId());
            System.out.println("interactiveEntity2.getId() = " + interactiveEntity2.getId());
            System.out.println("interactiveEntity3.getId() = " + interactiveEntity3.getId());
            System.out.println("interactiveEntityList.get(3) = " + interactiveEntityList.get(3).getId());
            System.out.println("interactiveEntityList.get(4) = " + interactiveEntityList.get(4).getId());
            System.out.println("interactiveEntityList.get(5) = " + interactiveEntityList.get(5).getId());

            List<InteractiveEntity> interactiveEntityList2 = interactiveEntityRepository.findPostsAndStatusUpdatesByUserIdAndStartingWithIdDesc(user.getId(), interactiveEntity1.getId(), 1);


            assertEquals(interactiveEntity2.getId(), interactiveEntityList2.get(0).getId());

            List<InteractiveEntity> interactiveEntityList3 = interactiveEntityRepository.findPostsAndStatusUpdatesByUserIdAndStartingWithIdDesc(user.getId(), interactiveEntity2.getId(), 2);

            System.out.println("interactiveEntityList3.get(0).getId() = " + interactiveEntityList3.get(0).getId());

            assertEquals(interactiveEntity3.getId(), interactiveEntityList3.get(0).getId());
            assertEquals(interactiveEntity3.getId() - 2, interactiveEntityList3.get(1).getId());
            assertEquals(2, interactiveEntityList3.size());

            List<InteractiveEntity> interactiveEntityList4 = interactiveEntityRepository.findPostsAndStatusUpdatesByUserIdAndStartingWithIdDesc(user.getId(), interactiveEntity3.getId() - 2, 3);

            assertEquals(2, interactiveEntityList4.size());

            assertEquals(interactiveEntity3.getId() - 3, interactiveEntityList4.get(0).getId());

            assertEquals(interactiveEntity3.getId() - 4, interactiveEntityList4.get(1).getId());

        }

        @Test
        @Order(3)
        @Transactional
        public void when_findPostsAndStatusUpdatesByUserIdFirstPage_Expect_objectsWithLimitAndStartWithLastId() {
            User user = userRepository.findByEmail("changli@gmail.com").get();

            Post post1 = Post.builder().user(user).text("Post1").likes(new ArrayList<>()).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
            Post post2 = Post.builder().user(user).text("Post2").likes(new ArrayList<>()).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
            Post post3 = Post.builder().user(user).text("Post3").likes(new ArrayList<>()).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
            postRepository.save(post1);
            postRepository.save(post2);
            postRepository.save(post3);

            List<InteractiveEntity> interactiveEntityList = interactiveEntityRepository.findAllPostsAndStatusUpdatesByUserId(user.getId());

            InteractiveEntity interactiveEntity1 = interactiveEntityList.get(0);
            InteractiveEntity interactiveEntity2 = interactiveEntityList.get(1);
            InteractiveEntity interactiveEntity3 = interactiveEntityList.get(2);

            List<InteractiveEntity> interactiveEntityList2 = interactiveEntityRepository.findPostsAndStatusUpdatesByUserIdFirstPage(user.getId(), 2);

            assertEquals(2, interactiveEntityList2.size());
            assertEquals(interactiveEntity1.getId(), interactiveEntityList2.get(0).getId());
            assertEquals(interactiveEntity2.getId(), interactiveEntityList2.get(1).getId());

            List<InteractiveEntity> interactiveEntityList3 = interactiveEntityRepository.findPostsAndStatusUpdatesByUserIdAndStartingWithIdDesc(user.getId(), interactiveEntityList2.get(1).getId(), 2);

            assertEquals(interactiveEntity3.getId(), interactiveEntityList3.get(0).getId());
            assertEquals(interactiveEntity3.getId() - 2, interactiveEntityList3.get(1).getId());
            assertEquals(2, interactiveEntityList3.size());

            List<InteractiveEntity> interactiveEntityList4 = interactiveEntityRepository.findPostsAndStatusUpdatesByUserIdAndStartingWithIdDesc(user.getId(), interactiveEntityList3.get(1).getId(), 2);

            assertEquals(interactiveEntity3.getId() - 3, interactiveEntityList4.get(0).getId());
            assertEquals(interactiveEntity3.getId() - 4, interactiveEntityList4.get(1).getId());
            assertEquals(2, interactiveEntityList4.size());
        }

        @Order(4)
        @Test
        @Transactional
        public void when_findAllPostsAndStatusUpdatesFirstPage_Expect_InteractiveEntityList() {
            User user = userRepository.findByEmail("changli@gmail.com").get();

            Post post1 = Post.builder().user(user).text("Post1").likes(new ArrayList<>()).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
            Post post2 = Post.builder().user(user).text("Post2").likes(new ArrayList<>()).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
            Post post3 = Post.builder().user(user).text("Post3").likes(new ArrayList<>()).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
            postRepository.save(post1);
            postRepository.save(post2);
            postRepository.save(post3);

            List<InteractiveEntity> allInteractiveEntities = interactiveEntityRepository.findAll();

            assertEquals(10, allInteractiveEntities.size());

            List<InteractiveEntity> interactiveEntityListLimitTwo = interactiveEntityRepository.findAllPostsAndStatusUpdatesFirstPage(2);

            assertEquals(2, interactiveEntityListLimitTwo.size());

            List<InteractiveEntity> interactiveEntityListLimitFive = interactiveEntityRepository.findAllPostsAndStatusUpdatesFirstPage(5);

            assertEquals(5, interactiveEntityListLimitFive.size());

            List<InteractiveEntity> interactiveEntityListPostAndStatusUpdatesOnly = interactiveEntityRepository.findAllPostsAndStatusUpdatesFirstPage(20);

            assertEquals(7, interactiveEntityListPostAndStatusUpdatesOnly.size());

        }

        @Order(5)
        @Test
        @Transactional
        public void when_findAllPostsAndStatusUpdatesStartingWithIdDesc_Expect_InteractiveEntityList() {
            User user = userRepository.findByEmail("changli@gmail.com").get();

            Post post1 = Post.builder().user(user).text("Post1").likes(new ArrayList<>()).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
            Post post2 = Post.builder().user(user).text("Post2").likes(new ArrayList<>()).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
            Post post3 = Post.builder().user(user).text("Post3").likes(new ArrayList<>()).createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
            postRepository.save(post1);
            postRepository.save(post2);
            postRepository.save(post3);

            List<InteractiveEntity> allInteractiveEntities = interactiveEntityRepository.findAll();

            assertEquals(10, allInteractiveEntities.size());

            List<InteractiveEntity> allPostsAndStatusUpdates = interactiveEntityRepository.findAllPostsAndStatusUpdatesFirstPage(20);

            List<InteractiveEntity> allPostsAndStatusUpdatesWithStartingId1 = interactiveEntityRepository.findAllPostsAndStatusUpdatesStartingWithIdDesc(allPostsAndStatusUpdates.get(0).getId(), 2);

            assertEquals(2, allPostsAndStatusUpdatesWithStartingId1.size());
            assertEquals(allPostsAndStatusUpdates.get(1).getId(), allPostsAndStatusUpdatesWithStartingId1.get(0).getId());
            assertEquals(allPostsAndStatusUpdates.get(2).getId(), allPostsAndStatusUpdatesWithStartingId1.get(1).getId());

            List<InteractiveEntity> allPostsAndStatusUpdatesWithStartingId2 = interactiveEntityRepository.findAllPostsAndStatusUpdatesStartingWithIdDesc(allPostsAndStatusUpdatesWithStartingId1.get(1).getId(), 2);

            assertEquals(2, allPostsAndStatusUpdatesWithStartingId2.size());
            assertEquals(allPostsAndStatusUpdates.get(3).getId(), allPostsAndStatusUpdatesWithStartingId2.get(0).getId());
            assertEquals(allPostsAndStatusUpdates.get(4).getId(), allPostsAndStatusUpdatesWithStartingId2.get(1).getId());
        }

    }
}
