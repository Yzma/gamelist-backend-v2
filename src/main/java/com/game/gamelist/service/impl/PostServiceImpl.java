package com.game.gamelist.service.impl;


import com.game.gamelist.entity.Post;
import com.game.gamelist.entity.User;
import com.game.gamelist.repository.PostRepository;
import com.game.gamelist.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;

    @Override
    public Post updatePostById(Long requestedId, Post post, User principal) {
        return null;
    }

    @Override
    public Post deletePostById(Long requestedId, User principal) {
        return null;
    }

    @Override
    public Set<Post> findAllPosts(User principal) {
        return null;
    }

    @Override
    public Post createPost(Post post, User principal) {
        return null;
    }

    @Override
    public Post findPostById(Long requestedId, User principal) {
        return null;
    }

    @Override
    public Set<Post> findAllPostsByUserId(User principal) {
        return null;
    }
}
