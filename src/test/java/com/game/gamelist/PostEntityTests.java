package com.game.gamelist;
import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@TestPropertySource(locations = "classpath:")
public class PostEntityTests {

    private User user1;
    private User user2;

    private Post post1;
    private Post post2;
    private Post post3;
    private Post post4;

    @BeforeEach
    void setUp() {

        user1 = User.builder().id(999L).email("test@example.com").username("testUser1").password("password").build();
        user2 = User.builder().id(1000L).email("test2@example.com").username("testUser2").password("password").build();


        post1 = Post.builder().id(99L).text("post1ByUser1").user(user1).build();
        post2 = Post.builder().id(100L).text("post2ByUser1").user(user1).build();
        post3 = Post.builder().id(101L).text("post3ByUser1").user(user1).build();

        post4 = Post.builder().id(102L).text("post4ByUser2").user(user2).build();

    }
    @Test
    public void post1ShouldHaveAUserFieldPointingToUser1() {
    assertThat(post1.getUser()).isEqualTo(user1);
    assertThat(post1.getUser().getId()).isEqualTo(999L);
    assertThat(post1.getUser().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DirtiesContext
    public void post4ShouldNotHaveAUserFieldPointingToUser1() {
    assertThat(post4.getUser()).isNotEqualTo(user1);
    assertThat(post4.getUser().getId()).isNotEqualTo(999L);
    assertThat(post4.getUser().getEmail()).isNotEqualTo("test@example.com");

    assertThat(post4.getUser()).isEqualTo(user2);
    assertThat(post4.getUser().getId()).isEqualTo(1000L);
    assertThat(post4.getUser().getEmail()).isEqualTo("test2@example.com");
    }

}
