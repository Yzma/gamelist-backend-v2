package com.game.gamelist.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.testcontainers.containers.PostgreSQLContainer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@DataJpaTest
public class UserGameRepositoryTests {

    static PostgreSQLContainer<?> container = new PostgreSQLContainer<>(
            "postgres:15-alpine"
    ).withUsername("changli").withPassword("123456").withDatabaseName("test_db");

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
