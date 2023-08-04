package com.game.gamelist.service;

import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.User;

import java.util.Set;

public interface PostService {

    Set<Post> findAllPostsByUserId(User principal);

    Post findPostById(Long requestedId, User principal);

    Post createPost(Post post, User principal);

    Post updatePostById(Long requestedId, Post post, User principal);

    Post deletePostById(Long requestedId, User principal);

    Set<Post> findAllPosts(User principal);
}
