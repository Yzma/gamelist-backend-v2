package com.game.gamelist.repository;

import com.game.gamelist.config.ContainersEnvironment;
import com.game.gamelist.entity.User;
import com.game.gamelist.exception.ResourceNotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ExtendWith(SpringExtension.class)
public class FollowRepositoryTests extends ContainersEnvironment {

    @Autowired
    private UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @BeforeEach
    void beforeEachTest() {

        User user = User.builder().email("user@gmail.com").username("user").password("password").bannerPicture("user banner").userPicture("user picture").build();

        userRepository.save(user);
    }
    @Test
    @Order(1)
    public void when_followers_or_following_Expect_EmptyList() {

        assertEquals(1, userRepository.findAll().size());
        User userFromDB = userRepository.findWithFollowersAndFollowingById(userRepository.findAll().get(0).getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        assertEquals("user", userFromDB.getUsername());

        assertEquals(0, userFromDB.getFollowers().size());
        assertEquals(0, userFromDB.getFollowing().size());
        userRepository.deleteById(userFromDB.getId());
    }

    @Test
    @Order(2)
    public void when_addFollower_Expect_FollowerAdded() {

        assertEquals(1, userRepository.findAll().size());
        User userFromDB = userRepository.findWithFollowersAndFollowingById(userRepository.findAll().get(0).getId()).orElseThrow(() -> new ResourceNotFoundException("User not found"));

        assertEquals("user", userFromDB.getUsername());
        assertEquals(0, userFromDB.getFollowers().size());
        assertEquals(0, userFromDB.getFollowing().size());

        User follower = User.builder().email("follower@gmail.com").username("follower").password("password").bannerPicture("follower banner").userPicture("follower picture").build();

        userRepository.save(follower);

        follower = userRepository.findWithFollowersAndFollowingByEmail("follower@gmail.com").orElseThrow(() -> new ResourceNotFoundException("Follower not found"));

        assertEquals(0, follower.getFollowers().size());
        assertEquals(0, follower.getFollowing().size());
        assertEquals("follower", follower.getUsername());


        follower.setFollowing(Set.of(userFromDB));

        userFromDB.setFollowers(Set.of(follower));

        userRepository.save(userFromDB);
        userRepository.save(follower);

        userFromDB = userRepository.findWithFollowersAndFollowingByEmail("user@gmail.com").orElseThrow(() -> new ResourceNotFoundException("User not found"));

        follower = userRepository.findWithFollowersAndFollowingByEmail("follower@gmail.com").orElseThrow(() -> new ResourceNotFoundException("Follower not found"));

        assertEquals(1, userFromDB.getFollowers().size());
        assertEquals(0, userFromDB.getFollowing().size());
        assertEquals(1, follower.getFollowing().size());
        assertEquals(0, follower.getFollowers().size());
        assertEquals("user", userFromDB.getUsername());

    }
}
