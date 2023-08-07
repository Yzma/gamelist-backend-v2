package com.game.gamelist.service.impl;

import com.game.gamelist.entity.Role;
import com.game.gamelist.entity.User;
import com.game.gamelist.exception.UserNotFoundException;
import com.game.gamelist.repository.UserRepository;
import com.game.gamelist.security.JwtIssuer;
import com.game.gamelist.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final JwtIssuer jwtIssuer;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder passwordEncoder;

    public Map<String, Object> attemptLogin(String email, String password) {
        var authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        var principal = (User) authentication.getPrincipal();
        return formReturnedData(principal);
    }

    public Map<String, Object> attemptSignup(String email, String password, String username) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("User with this email already exists");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setListsOrder("planning,playing,completed,paused,dropped,justAdded");
        user.setActive(true);


        Set<Role> roles = new HashSet<>();
        roles.add(Role.ROLE_USER);
        user.setRoles(roles);

        var savedUser = userRepository.save(user);
        return formReturnedData(savedUser);
    }

    private Map<String, Object> formReturnedData(User user) {
        var roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        Map<String, Object> returnedData = new HashMap<>();
        returnedData.put("token", jwtIssuer.issue(user.getId(), user.getEmail(), roles));

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("email", user.getEmail());
        userMap.put("username", user.getUsername());
        userMap.put("bannerPicture", user.getBannerPicture() != null ? user.getBannerPicture() : "");
        userMap.put("userPicture", user.getUserPicture() != null ? user.getUserPicture() : "");

        returnedData.put("user", userMap);

        return returnedData;
    }

    public User getUser(int id) {
        return userRepository.findById((long) id).orElseThrow(() -> new UserNotFoundException("User not found. Id: " + id));
    }
}
