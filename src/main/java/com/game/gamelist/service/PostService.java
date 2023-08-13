package com.game.gamelist.service;

import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public interface PostService {

    Set<Post> findAllPostsByUserId(User principal);

    Post findPostById(Long requestedId, User principal);

    Post createPost(Post post, User principal);

    Post updatePostById(Long requestedId, Post post, User principal);

    Post deletePostById(Long requestedId, User principal);

    List<Post> findAllPosts(User principal);
}
