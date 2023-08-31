package com.game.gamelist.repository;

import com.game.gamelist.config.ContainersEnvironment;
import com.game.gamelist.entity.Comment;
import com.game.gamelist.entity.LikeEntity;
import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.User;
import com.game.gamelist.exception.ResourceNotFoundException;
import com.game.gamelist.projection.CommentView;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
public class CommentRepositoryTests extends ContainersEnvironment {

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Transactional
    public void contextLoads() {
        assertNotEquals(null, commentRepository);
        assertNotEquals(null, postRepository);
        assertNotEquals(null, likeRepository);
        assertNotEquals(null, entityManager);
    }

    @Test
    @Transactional
    public void whenFindAll_Expect_EmptyList() {
        List<Comment> commentList = commentRepository.findAll();
        assertEquals(0, commentList.size());
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class CommentRepositoryCRUDTests {

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
            post1.setCreatedAt(LocalDateTime.now());
            post1.setUpdatedAt(LocalDateTime.now());
            postRepository.save(post1);

            Post post2 = new Post();
            post2.setText("Another Post");
            post2.setUser(user);
            post2.setCreatedAt(LocalDateTime.now());
            post2.setUpdatedAt(LocalDateTime.now());
            postRepository.save(post2);

            LikeEntity like = new LikeEntity();
            like.setInteractiveEntity(post1);
            like.setUser(user);
            likeRepository.save(like);
        }

        @Test
        @Transactional
        @Order(1)
        public void test_beforeEach_context() {
            User user = userRepository.findByEmail("changli@gmail.com").orElseThrow(() -> new ResourceNotFoundException("User not found"));

            assertEquals("changli", user.getUsername());
            assertEquals("changli@gmail.com", user.getEmail());

            Post post = postRepository.findById(1L).orElseThrow(() -> new ResourceNotFoundException("Post not found"));
            entityManager.refresh(post);

            assertEquals("Hello World", post.getText());
            assertEquals("changli", post.getUser().getUsername());
            assertEquals(1, post.getLikes().size());
        }

        @Test
        @Transactional
        @Order(2)
        public void when_saveComment_onPost_expect_success() {

            assertEquals(0, commentRepository.findAll().size());
            Post post = postRepository.findAll().get(0);
            User owner = userRepository.findAll().get(0);

            Comment comment = new Comment();
            comment.setText("This is a comment");
            comment.setInteractiveEntity(post);
            comment.setUser(owner);

            post.getComments().add(comment);

            commentRepository.save(comment);
            List<Comment> commentList = commentRepository.findAll();
            assertEquals(1, commentList.size());
            assertEquals(1, post.getComments().size());
            Post postFromDB = postRepository.findAll().get(0);

            assertEquals(1, postFromDB.getComments().size());
            assertEquals("This is a comment", postFromDB.getComments().get(0).getText());
            assertEquals("changli", postFromDB.getComments().get(0).getUser().getUsername());
        }

        @Test
        @Transactional
        @Order(3)
        public void when_saveOnComment_Expect_Success() {
            assertEquals(0, commentRepository.findAll().size());
            Post post = postRepository.findAll().get(0);
            User owner = userRepository.findAll().get(0);

            Comment comment = new Comment();
            comment.setText("This is a comment");
            comment.setInteractiveEntity(post);
            comment.setUser(owner);

            post.getComments().add(comment);
            commentRepository.save(comment);

            User commentOnCommentOwner = User.builder().username("commentOnCommentOwner").email("commentowner@gmail.com").password("123456").bannerPicture("banner picture").userPicture("user picture").build();
            userRepository.save(commentOnCommentOwner);

            Comment commentOfComment = new Comment();
            commentOfComment.setText("This is a comment of comment");
            commentOfComment.setUser(commentOnCommentOwner);
            commentOfComment.setInteractiveEntity(comment);
            commentRepository.save(commentOfComment);

            assertEquals(2, commentRepository.findAll().size());

            Comment commentOfCommentFromDB = commentRepository.findAll().get(1);

            assertEquals("This is a comment of comment", commentOfCommentFromDB.getText());
            assertEquals("commentOnCommentOwner", commentOfCommentFromDB.getUser().getUsername());
            assertEquals("This is a comment", ((Comment)commentOfCommentFromDB.getInteractiveEntity()).getText());

        }

        @Test
        @Order(4)
        @Transactional
        public void when_getAllCommentByInteractiveEntityId_Expect_allComments() {
            assertEquals(0, commentRepository.findAll().size());
            Post post = postRepository.findAll().get(0);
            User owner = userRepository.findAll().get(0);

            Post newPost = Post.builder().text("new post").user(owner).build();
            postRepository.save(newPost);

            Comment comment1 = new Comment();
            comment1.setText("This is a comment1");
            comment1.setInteractiveEntity(post);
            comment1.setUser(owner);

            Comment comment2 = new Comment();
            comment2.setText("This is a comment2");
            comment2.setInteractiveEntity(post);
            comment2.setUser(owner);

            Comment comment3 = new Comment();
            comment3.setText("This is a comment3");
            comment3.setInteractiveEntity(post);
            comment3.setUser(owner);

            Comment comment4 = new Comment();
            comment4.setText("This is a comment4");
            comment4.setInteractiveEntity(newPost);
            comment4.setUser(owner);

            commentRepository.save(comment1);
            commentRepository.save(comment2);
            commentRepository.save(comment3);
            commentRepository.save(comment4);
            entityManager.refresh(post);

            User commentOnCommentOwner = User.builder().username("commentOnCommentOwner").email("commentowner@gmail.com").password("123456").bannerPicture("banner picture").userPicture("user picture").build();
            userRepository.save(commentOnCommentOwner);

            Comment commentOfComment = new Comment();
            commentOfComment.setText("This is a comment of comment");
            commentOfComment.setUser(commentOnCommentOwner);
            commentOfComment.setInteractiveEntity(comment1);
            commentRepository.save(commentOfComment);

            List<Comment> commentList = commentRepository.findAllByInteractiveEntityId(post.getId());
            assertEquals(3, commentList.size());
        }

        @Test
        @Order(5)
        @Transactional
        public void when_findProjectedByInteractiveEntityId_expect_listOfCommentView() {
            assertEquals(0, commentRepository.findAll().size());
            Post post = postRepository.findAll().get(0);
            User owner = userRepository.findAll().get(0);

            User commentOnCommentOwner = User.builder().username("commentOnCommentOwner").email("commentowner@gmail.com").password("123456").bannerPicture("banner picture").userPicture("user picture").build();
            userRepository.save(commentOnCommentOwner);

            Post newPost = Post.builder().text("new post").user(owner).build();
            postRepository.save(newPost);

            Comment postComment = Comment.builder().interactiveEntity(post).text("post original comment").user(owner).build();
            commentRepository.save(postComment);

            Comment comment1 = new Comment();
            comment1.setText("This is a comment1");
            comment1.setInteractiveEntity(postComment);
            comment1.setUser(owner);

            Comment comment2 = new Comment();
            comment2.setText("This is a comment2");
            comment2.setInteractiveEntity(postComment);
            comment2.setUser(owner);

            Comment comment3 = new Comment();
            comment3.setText("This is a comment3");
            comment3.setInteractiveEntity(postComment);
            comment3.setUser(owner);

            Comment comment4 = new Comment();
            comment4.setText("This is a comment4");
            comment4.setInteractiveEntity(newPost);
            comment4.setUser(owner);

            commentRepository.save(comment1);
            commentRepository.save(comment2);
            commentRepository.save(comment3);
            commentRepository.save(comment4);
            entityManager.refresh(post);

            Comment commentOfComment = new Comment();
            commentOfComment.setText("This is a comment of comment");
            commentOfComment.setUser(commentOnCommentOwner);
            commentOfComment.setInteractiveEntity(comment1);
            commentRepository.save(commentOfComment);

            List<CommentView> commentViewList = commentRepository.findProjectedByInteractiveEntityId(postComment.getId());

            assertEquals(3, commentViewList.size());
            assertEquals("Banner Picture URL", commentViewList.get(0).getUser().getBannerPicture());
            assertEquals("User Picture URL", commentViewList.get(0).getUser().getUserPicture());
            assertThat(commentViewList.get(0).getUser(), not((hasProperty("password", is("123456")))));
            assertEquals("changli", commentViewList.get(0).getUser().getUsername());
            assertThat(commentViewList.get(0).getUser(), not((hasProperty("email"))));
        }
    }
}
