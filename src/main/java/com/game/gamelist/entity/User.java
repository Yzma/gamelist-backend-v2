package com.game.gamelist.entity;


import com.fasterxml.jackson.annotation.*;
import com.game.gamelist.validator.RoleSubset;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
@JsonFilter("UserInfoNeeded")
public class User implements UserDetails {

    @Id
    @GeneratedValue
    private Long id;

    private String username;

    @JsonProperty("email_address")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password_digest")
    private String password;

    @Column(name = "banner_picture")
    @JsonProperty("banner_picture")
    private String bannerPicture;

    @Column(name = "user_picture")
    @JsonProperty("user_picture")
    private String userPicture;

    private String bio;

    @Column(name = "is_active")
    @JsonProperty("is_active")
    private boolean isActive;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "`listsOrder`")
    private String listsOrder;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
    @JsonProperty("roles")
    @Enumerated(EnumType.STRING)
    @RoleSubset(anyOf = {Role.ROLE_USER, Role.ROLE_ADMIN})
    private Set<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    @Column(name = "posts")
    private List<Post> posts;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    @Column(name = "game_journals")
    private List<GameJournal> gameJournals;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("user")
    @Column(name = "user_games")
    private Set<UserGame> userGames;

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
