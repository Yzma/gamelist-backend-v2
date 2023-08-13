package com.game.gamelist.config;

import com.game.gamelist.entity.User;
import com.game.gamelist.security.UserPrincipleAuthenticationToken;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@TestConfiguration
public class SecurityTestConfig {

    @Bean
    public Auth0JwtTestUtils auth0JwtTestUtils() {
        return new Auth0JwtTestUtils();
    }

    public static class Auth0JwtTestUtils {

        public void mockAuthentication(User user) {
            Authentication authentication = new UserPrincipleAuthenticationToken(user);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }
}