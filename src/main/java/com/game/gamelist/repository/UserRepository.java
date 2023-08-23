package com.game.gamelist.repository;

import com.game.gamelist.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u.listsOrder FROM users u WHERE u.id = ?1")
    String findListsOrderById(Long id);
}
