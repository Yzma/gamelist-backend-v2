package com.game.gamelist.security;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.game.gamelist.entity.Role;
import com.game.gamelist.entity.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtToPrincipalConverter {
    public User convert(DecodedJWT jwt) {
        return User.builder().id(Long.valueOf(jwt.getSubject())).email(jwt.getClaim("email").asString()).roles(extractAuthorities(jwt)).build();
    }

    private Set<Role> extractAuthorities(DecodedJWT jwt) {
        Claim claim = jwt.getClaim("roles");

        if (claim.isNull() || claim.isMissing()) {
            return Set.of();
        }
        return claim.asList(SimpleGrantedAuthority.class).stream()
                .map(simpleGrantedAuthority -> Role.valueOf(simpleGrantedAuthority.getAuthority()))
                .collect(Collectors.toSet());
    }
}
