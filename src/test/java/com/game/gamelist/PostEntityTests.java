package com.game.gamelist;
import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.User;

import jakarta.persistence.EntityManager;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:")
public class PostEntityTests {

    @Autowired
    private EntityManager entityManager;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = User.builder().id(999L).email("test@example.com").username("testUser1").password("password").build();
        user2 = User.builder().id(1000L).email("test2@example.com").username("testUser2").password("password").build();

        Post post1 = Post.builder().id(99L).text("post1ByUser1").user(user1).build();
        Post post2 = Post.builder().id(100L).text("post2ByUser1").user(user1).build();
        Post post3 = Post.builder().id(101L).text("post3ByUser1").user(user1).build();

        Post post4 = Post.builder().id(102L).text("post4ByUser2").user(user2).build();
//
//        entityManager.persist(user1);
//        entityManager.persist(user2);
//        entityManager.persist(post1);
//        entityManager.persist(post2);
//        entityManager.persist(post3);
//        entityManager.persist(post4);
    }
    @Test
    public void post1ShouldHaveAUserFieldPointingToUser1() {
        Post post = entityManager.find(Post.class, 99L);

        assertThat(post.getUser().getId()).isEqualTo(999L);
    }

}
