package com.game.gamelist.security;

import com.game.gamelist.entity.User;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Objects;

public class UserPrincipleAuthenticationToken extends AbstractAuthenticationToken {
    private final User principal;

    public UserPrincipleAuthenticationToken(User principal) {
        super(principal.getAuthorities());
        this.principal = principal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public User getPrincipal() {
        return principal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserPrincipleAuthenticationToken that = (UserPrincipleAuthenticationToken) o;
        return Objects.equals(principal, that.principal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(principal);
    }
}
