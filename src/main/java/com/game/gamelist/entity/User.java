package com.game.gamelist.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.game.gamelist.validator.RoleSubset;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
//@JsonFilter("UserInfoNeeded")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    @Size(min = 2, message = "Username should have at least 2 characters")
    private String username;

    @Email(message = "Email should be valid")
    @JsonProperty("email_address")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @NotEmpty(message = "Password is required")
    @Column(name = "password_digest")
    private String password;

    private String bannerPicture;

    private String userPicture;

    private String bio;

    private boolean isActive;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Column(name = "listsOrder")
    private String listsOrder;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @RoleSubset(anyOf = {Role.ROLE_USER, Role.ROLE_ADMIN})
    private Set<Role> roles;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(innerRole -> new SimpleGrantedAuthority(innerRole.name()))
                .toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}