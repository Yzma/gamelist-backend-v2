package com.game.gamelist.repository;


import com.game.gamelist.config.ContainersEnvironment;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class UserGameRepositoryTests extends ContainersEnvironment {

    @Autowired
    private UserGameRepository userGameRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void testTest() {
        assertNotEquals(null, userGameRepository);
        assertNotEquals(null, userRepository);
        System.out.println("test");
    }

}
