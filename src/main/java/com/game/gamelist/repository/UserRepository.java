package com.game.gamelist.repository;

import com.game.gamelist.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u.listsOrder FROM users u WHERE u.id = ?1")
    String findListsOrderById(Long id);

//    @Query("SELECT u FROM users u LEFT JOIN FETCH u.followers LEFT JOIN FETCH u.following WHERE u.id = :id")
    @EntityGraph(attributePaths = {"followers", "following"})
    Optional<User> findWithFollowersAndFollowingById(Long id);

    @EntityGraph(attributePaths = {"followers", "following"})
    Optional<User> findWithFollowersAndFollowingByEmail(String email);
}
